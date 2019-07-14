package org.robinbird;

import static org.robinbird.clustering.ClusteringMethodType.AGGLOMERATIVE_CLUSTERING;
import static org.robinbird.model.AnalysisJob.Language.JAVA8;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.robinbird.clustering.AgglomerativeClusteringNodeMatchers;
import org.robinbird.clustering.ClusteringMethod;
import org.robinbird.clustering.ClusteringMethodFactory;
import org.robinbird.clustering.ClusteringNode;
import org.robinbird.clustering.ClusteringNodeFactory;
import org.robinbird.clustering.RelationSelectors;
import org.robinbird.model.AnalysisContext;
import org.robinbird.model.AnalysisJob;
import org.robinbird.model.ComponentCategory;
import org.robinbird.presentation.GMLPresentation;
import org.robinbird.presentation.PlantUMLPresentation;
import org.robinbird.presentation.Presentation;
import org.robinbird.presentation.PresentationType;
import org.robinbird.repository.ComponentRepository;
import org.robinbird.repository.dao.ComponentEntityDao;
import org.robinbird.repository.dao.ComponentEntityDaoH2Factory;
import org.robinbird.util.StringAppender;
import org.robinbird.util.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class Application {

	private final static String SHELL_DIR = "shell.dir";

	private String shellDir;

	public void run(CommandLine commandLine) {
		shellDir = System.getProperty(SHELL_DIR);
		if (shellDir == null) {
			shellDir = System.getProperty("user.dir");
			String msg =
					"Cannot get current shell directory. Set shellDir with user.dir system property which is " + shellDir;
			System.out.println(msg);
			log.info(msg);
		}
		log.info("\n" + Utils.printMemoryInfo());

		return;
/*
		final AnalysisJob analysisJob = new AnalysisJob(JAVA8);
		analysisJob.addPath(getRootPath(commandLine));

		//AnalysisUnit au = new AnalysisUnit(JAVA8);
		//au.addPath(getRootPath(commandLine));

		List<Pattern> terminalPatterns = convertStringsToPatterns(commandLine.getOptionValues("tc"));
		List<Pattern> excludedPatterns = convertStringsToPatterns(commandLine.getOptionValues("ec"));

		final ComponentEntityDao componentEntityDao = ComponentEntityDaoH2Factory.createDao();
		final ComponentRepository componentRepository = new ComponentRepository(componentEntityDao);
		final AnalysisContext analysisContext = analysisJob.analysis(componentRepository, terminalPatterns, excludedPatterns);

		// based on command line option, trying clustering.
		final ClusteringNodeFactory clusteringNodeFactory = new ClusteringNodeFactory(componentRepository);
		final ClusteringMethodFactory clusteringMethodFactory = new ClusteringMethodFactory(clusteringNodeFactory);

		// get clustering method from command line.
		final ClusteringMethod clusteringMethod = clusteringMethodFactory.create(AGGLOMERATIVE_CLUSTERING, 1.0, 3.0);
		final List<ClusteringNode> clusteringNodes = clusteringMethod.cluster(analysisContext.getComponents(ComponentCategory.CLASS),
																			  RelationSelectors::getClassRelations,
																			  AgglomerativeClusteringNodeMatchers::matchScoreRange);






								 //final TypeDao typeDao = TypeDaoFactory.createDao(); // todo: add option to consider stored db file
								 //final TypeRepository typeRepository = new TypeRepositoryImpl(typeDao);
								 //AnalysisContext ac = au.analysis(typeRepository, terminalPatterns, excludedPatterns);
								 //Presentation acPresent = createPresentation(getPresentationType(commandLine), commandLine);
								 //System.out.println(acPresent.present(ac));



								 // based on old model
		AnalysisUnit au = new AnalysisUnit(JAVA8);
		au.addPath(getRootPath(commandLine));

		List<Pattern> terminalPatterns = convertStringsToPatterns(commandLine.getOptionValues("tc"));
		List<Pattern> excludedPatterns = convertStringsToPatterns(commandLine.getOptionValues("ec"));

		AnalysisContext ac = au.analysis(terminalPatterns, excludedPatterns);
		Presentation acPresent = createPresentation(getPresentationType(commandLine), commandLine);
		System.out.print(acPresent.present(ac));*/
	}

	private Path getRootPath(CommandLine commandLine) {
		String rootPathStr = commandLine.getOptionValue("r");
		if (rootPathStr == null) {
			rootPathStr = "./";
		}
		Path rootPath = Paths.get(rootPathStr);
		if (rootPath.isAbsolute()) {
			return rootPath;
		} else {
			return (Paths.get(shellDir, rootPathStr));
		}
	}

	private List<Pattern> convertStringsToPatterns(String[] strings) {
		return Optional.ofNullable(strings)
				.map(strs -> Arrays.stream(strs).map(Pattern::compile).collect(Collectors.toList()))
				.orElse(null);
	}

	private PresentationType getPresentationType(CommandLine commandLine) {
		String presentation = commandLine.getOptionValue("p");
		// default presentation option
		if (presentation == null) {
			presentation = PresentationType.PLANTUML.name();
		}
		return PresentationType.valueOf(presentation);
	}

	private Presentation createPresentation(PresentationType ptype, CommandLine commandLine) {
		Presentation presentation;
		switch (ptype) {
			case GML:
				presentation = new GMLPresentation();
				break;
			case PLANTUML:
			default:
				presentation = new PlantUMLPresentation();
				break;
		}
		return presentation;
	}

	public static void main(String[] args) {
		Path currentRelativePath = Paths.get("");
		System.out.println(currentRelativePath.toString());
		Options options = buildOptions();
		CommandLine commandLine = parseCommandLine(args, options);
		if (commandLine == null || commandLine.hasOption("h")) {
			printHelpMessages(options);
			return;
		}
		Application app = new Application();
		app.run(commandLine);
	}

	private static CommandLine parseCommandLine(@NonNull final String[] args, @NonNull final Options options) {
		final CommandLineParser parser = new DefaultParser();
		try {
			return parser.parse(options, args);
		} catch (Throwable e) {
			log.error("{}", e.getMessage(), e);
			return null;
		}
	}

	private static void printHelpMessages(@NonNull final Options options) {
		StringAppender sa = new StringAppender();
		sa.appendLine("Usage: robinbird option-type option-value ...\n");
		sa.appendLine("Examples:");
		sa.appendLine("robinbird -root your_root_path_for_source_codes");
		sa.appendLine("  . This will generate PlantUML class diagram script for the given root");
		sa.appendLine("robinbird -r root_path -excluded-class ExcludedClass.*");
		sa.appendLine("  . This will generate PlantUML class diagrams from root_path excluding classes matched with Java regular " +
							  "expression 'EscludedClass.*'\n");
		sa.appendLine("Options:");

		final HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(formatter.getWidth()*2, sa.toString(), null, options, null);
	}

	private static Options buildOptions() {
		final Option help = Option.builder("h")
								  .longOpt("help")
								  .desc("Print help messages.")
								  .build();
		final Option root = Option.builder("r")
								  .longOpt("root")
								  .desc("specify root path of source codes")
								  .hasArg()
								  .build();
		final Option presentation =
				Option.builder("p")
					  .longOpt("presentation")
					  .desc("set presentation type. default is PLANTUML. " +
							"Currently, supported types are PLANTUML and GML (Graph Modeling Language).")
					  .hasArg()
					  .build();
		final Option db =
				Option.builder("db")
					  .longOpt("database-file")
					  .desc("Local h2 database file")
					  .hasArg()
					  .build();
		final Option terminalClass =
				Option.builder("tc")
					  .longOpt("terminal-class")
					  .numberOfArgs(Option.UNLIMITED_VALUES)
					  .desc("Classes matched with this regular expression will be only shown their names in class diagram")
					  .build();
		final Option excludedClass =
				Option.builder("ec")
					  .longOpt("excluded-class")
					  .numberOfArgs(Option.UNLIMITED_VALUES)
					  .desc("Classes matched with this regular expression will not be shown in class diagram")
					  .build();
		final Option clusteringType =
				Option.builder("ct")
					  .longOpt("clustering-type")
					  .desc("Robinbird provides abstrated components diagram. Currently, only has AGGLOMERATIVE clustering")
					  .hasArg()
					  .build();
		final Option params =
				Option.builder("cp")
					  .longOpt("clustering-parameters")
					  .desc("Parameters for clustering. Depends on clustering type.")
					  .hasArgs()
					  .build();
		return new Options().addOption(help)
							.addOption(root)
							.addOption(presentation)
							.addOption(db)
							.addOption(terminalClass)
							.addOption(excludedClass)
							.addOption(clusteringType)
							.addOption(params);
	}
}
