package me.trqhxrd.grapesrpg

import com.google.common.reflect.ClassPath
import me.trqhxrd.grapesrpg.api.item.Item
import me.trqhxrd.grapesrpg.api.recipe.Recipe
import me.trqhxrd.grapesrpg.game.item.attribute.*
import me.trqhxrd.grapesrpg.impl.item.attribute.AttributeRegistry
import me.trqhxrd.grapesrpg.listener.EntityDamageByEntityListener
import me.trqhxrd.grapesrpg.listener.PlayerInteractListener
import me.trqhxrd.grapesrpg.listener.PlayerJoinListener
import me.trqhxrd.menus.Menus
import org.apache.maven.repository.internal.MavenRepositorySystemUtils
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.collection.CollectRequest
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory
import org.eclipse.aether.graph.Dependency
import org.eclipse.aether.repository.LocalRepository
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.resolution.DependencyRequest
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory
import org.eclipse.aether.spi.connector.transport.TransporterFactory
import org.eclipse.aether.transport.file.FileTransporterFactory
import org.eclipse.aether.transport.http.HttpTransporterFactory
import org.eclipse.aether.util.graph.visitor.PreorderNodeListGenerator
import java.io.File
import java.nio.file.Files
import java.util.logging.Logger
import java.util.stream.Stream

/**
 * This class contains some utility methods used by the plugin.
 * @author Trqhxrd
 * @since 1.0
 */
object GrapesRPG {
    /**
     * This field contains the [Plugin], that owns the API.
     * In most of the cases that's an instance of [Main].
     */
    lateinit var plugin: JavaPlugin

    /**
     * This field contains the [Logger] of the plugin passed into [GrapesRPG.init]
     */
    lateinit var logger: Logger

    /**
     * This field contains an [AttributeRegistry].
     * Every [me.trqhxrd.grapesrpg.api.item.attribute.Attribute] has to be registered here.
     * If you want to create your own [me.trqhxrd.grapesrpg.api.item.attribute.Attribute],
     * have a look at some predefined attributes.
     */
    val attributes = AttributeRegistry()

    val recipes = mutableListOf<Recipe>()

    /**
     * Whether debug mode is enabled. When debug mode is enabled the plugin does stuff like giving out items on join.
     */
    var debugMode: Boolean = false
        set(value) {
            if (field) return
            field = value
            this.enableDebugMode()
        }

    /**
     * This method needs to be called for the API to initialize.
     */
    fun init(plugin: JavaPlugin) {
        this.plugin = plugin
        this.logger = this.plugin.logger

        this.plugin.config.options().copyDefaults(true)
        this.plugin.saveConfig()
        this.debugMode = this.plugin.config.getBoolean("debug")

        Menus.enable(this.plugin)

        this.downloadDependencies()
        this.setupMetrics()
        this.setupAttributes()
        this.registerItems("me.trqhxrd.grapesrpg.game")
        this.setupListeners()
    }

    /**
     * This function downloads and installs dependencies listed in the "dependencies" section in the config.
     */
    private fun downloadDependencies() {
        this.logger.info("Downloading dependencies... This may take some time.")
        this.logger.info("Setting up repository structure.")
        val locator = MavenRepositorySystemUtils.newServiceLocator()
        locator.addService(RepositoryConnectorFactory::class.java, BasicRepositoryConnectorFactory::class.java)
        locator.addService(TransporterFactory::class.java, FileTransporterFactory::class.java)
        locator.addService(TransporterFactory::class.java, HttpTransporterFactory::class.java)

        this.logger.info("Indexing repositories")
        val repositorySystem = locator.getService(RepositorySystem::class.java)
        val session = MavenRepositorySystemUtils.newSession()
        val localRepository = LocalRepository(File(this.plugin.dataFolder, "cache"))
        session.localRepositoryManager = repositorySystem.newLocalRepositoryManager(session, localRepository)

        val repositories = mutableListOf<RemoteRepository>()
        for (repo in this.plugin.config.getConfigurationSection("dependencies.repositories")!!.getKeys(false)) {
            val section = this.plugin.config.getConfigurationSection("dependencies.repositories.$repo")!!
            val id = section.getString("id")
            val url = section.getString("url")

            repositories.add(RemoteRepository.Builder(id, "default", url).build())
            this.logger.info("Indexed repository $id ($url)")
        }

        this.logger.info("Indexing dependencies")
        val dependencies = mutableListOf<Dependency>()
        for (dep in this.plugin.config.getConfigurationSection("dependencies.artifacts")!!.getKeys(false)) {
            val section = this.plugin.config.getConfigurationSection("dependencies.artifacts.$dep")!!
            val group = section.getString("groupId")
            val artifact = section.getString("artifactId")
            val version = section.getString("version")
            val scope = section.getString("scope")

            this.logger.info("Found dependency $group:$artifact:$version ($scope)")
            dependencies.add(Dependency(DefaultArtifact("$group:$artifact:$version"), scope))
        }

        this.logger.info("Collecting dependencies...")
        for (dep in dependencies) {
            val artifact = "${dep.artifact.groupId}:${dep.artifact.artifactId}:${dep.artifact.version}"

            val jar = File(
                this.plugin.dataFolder.parentFile,
                "${dep.artifact.artifactId}-${dep.artifact.version}.jar"
            )
            if (jar.exists()) {
                this.logger.info("Artifact $artifact already exists. Skipping.")
                continue
            }

            this.logger.info("Collecting $artifact.")
            val collectRequest = CollectRequest(dep, repositories)
            val node = repositorySystem.collectDependencies(session, collectRequest).root
            this.logger.info("Collected $artifact.")

            this.logger.info("Collecting dependencies of $artifact.")
            val dependencyRequest = DependencyRequest()
            dependencyRequest.root = node
            repositorySystem.resolveDependencies(session, dependencyRequest)
            this.logger.info("Collected dependencies of $artifact.")

            val nodeListGenerator = PreorderNodeListGenerator()
            node.accept(nodeListGenerator)

            val source = File(nodeListGenerator.classPath.split(":")[0])
            val target = File(this.plugin.dataFolder.parentFile, source.name)

            if (!target.exists()) {
                this.logger.info("Failed to find $artifact in plugins directory. Installing...")
                Files.copy(source.toPath(), target.toPath())

                val pl = this.plugin.server.pluginManager.loadPlugin(target)!!
                this.plugin.server.pluginManager.enablePlugin(pl)
            } else this.logger.info("Artifact $artifact is already installed on the server.")
        }
        this.logger.info("Collected all dependencies.")
    }

    /**
     * This method contains all things, that need to be executed when enabling debug mode.
     */
    private fun enableDebugMode() {
        this.logger.warning("Debug-mode enabled! Restart server to disable")
        PlayerJoinListener(this.plugin)
    }

    private fun setupMetrics() {
        Metrics(this.plugin, 14961)
    }

    private fun registerItems(path: String) {
        ClassPath.from(this::class.java.classLoader).getTopLevelClassesRecursive(path).stream()
            .filter { ci -> ci.packageName.startsWith(path) }
            .map { ci -> Class.forName(ci.name) }
            .filter { c -> Item::class.java.isAssignableFrom(c) }
            .flatMap { c -> Stream.of(*c.constructors) }
            .filter { c -> c.parameters.isEmpty() }
            .map { c -> c.newInstance() as Item }
            .map { i -> i.recipe }
            .filter { r -> r != null }
            .forEach { r -> this.recipes.add(r!!) }
    }

    private fun setupAttributes() {
        this.attributes.addAttribute(Damaging())
        this.attributes.addAttribute(Durability())
        this.attributes.addAttribute(Lore())
        this.attributes.addAttribute(Name())
        this.attributes.addAttribute(Todo())
        this.attributes.addAttribute(Material())
    }

    private fun setupListeners() {
        EntityDamageByEntityListener()
        PlayerInteractListener()
    }
}
