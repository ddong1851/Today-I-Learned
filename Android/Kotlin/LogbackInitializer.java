public class LogbackInitializer {

    public static final String CONFIG_FILE_NAME = "logback.xml";

    public static void init(Context context) {
        try {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(loggerContext);
            // Call context.reset() to clear any previous configuration, e.g. default
            // configuration. For multi-step configuration, omit calling context.reset().
            loggerContext.reset();

            AssetManager assets = context.getAssets();
            InputStream is = assets.open(CONFIG_FILE_NAME);
            configurator.doConfigure(is);
        } catch (JoranException | IOException | ClassCastException exception) {
            // StatusPrinter will handle this
        }
    }
}
