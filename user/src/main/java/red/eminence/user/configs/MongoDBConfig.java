package red.eminence.user.configs;
//@Data
//@EqualsAndHashCode (callSuper = false)
//@Configuration
//@ConfigurationProperties (prefix = "core.db")
//// @EnableMongoRepositories (basePackageClasses = {RepositoryPointer.class, RepositoryPointerCommons.class})
//public class MongoDBConfig extends AbstractReactiveMongoConfiguration
//{
//    private String  database;
//    private String  host;
//    private int     port;
//    // These 3 are not used under the current Mongo context.
//    private String  user;
//    private String  password;
//    private boolean ssl;
//
//    @Override
//    public @NonNull String getDatabaseName ()
//    {
//        return database;
//    }
//
//    @Override
//    protected boolean autoIndexCreation ()
//    {
//        return true;
//    }
//
//    @Override
//    protected void configureClientSettings (MongoClientSettings.Builder builder)
//    {
//        builder.applyToClusterSettings(settings -> {
//            settings.hosts(List.of(new ServerAddress(this.host, this.port)));
//        });
//        if (StringUtils.isNotBlank(this.getUser())) {
//            // only add credential system if they are defined
//            builder.credential(MongoCredential.createCredential(this.getUser(), this.getDatabaseName(), this.getPassword().toCharArray()));
//        }
//    }
//}
