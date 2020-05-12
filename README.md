# ambient-weather-logger
Queries data from Ambient Weather devices and logs it to a MySQL database.

Given that this pulls data from Ambient Weather and then inserts it into 
some MySQL database, here are the external things you'll need.

- An Application and API key from Ambient Weather
- A MySQL database instance. You'll need a DB user and password along 
with a datbase (ambient_weather) that the user has permissions on.

### Installing the .deb package.

From a Debian/Ubuntu/Pop you can download a prebuilt amd64 deb from 
Gemfury and install it with little effort.

```
wget -O ambient-weather-logger_1.0.0-1_amd64.deb https://manage.fury.io/2/indexes/deb/rsv-code/download/ambient-weather-logger-1.0.0-1-amd64
```

Then install.

```
sudo apt install ./ambient-weather-logger_1.0.0-1_amd64.deb
```

The package will be installed in /opt/ambient-weather-logger. In order for the 
application to run propery, there are a variety of options that should be 
provided to the app. Set these in the app config file.

/opt/ambient-weather-logger/lib/app/ambient-weather-logger.cfg:
```
[JavaOptions]
-DappKey=<appKey>
-DapiKey=<apiKey>
-DmysqlHost=<host>
-DmysqlUserName=<user>
-DmysqlPassword=<pass>
```

### Running

If you installed the applicaiton from .dep package and configured it correctly 
then you should just be able to run the following.

```
/opt/ambient-weather-logger/bin/ambient-weather-logger
```

If alll went well you should see two tables in the DB now.

```
mysql> show tables;
+---------------------------+
| Tables_in_ambient_weather |
+---------------------------+
| datarecords               |
| devices                   |
+---------------------------+
2 rows in set (0.01 sec)
```

You want them to run on a schedule? Here's the easy way to do that.

```
> crontab -e
and then set it to run every 2 minutes.
*/2 * * * *	/opt/ambient-weather-logger/bin/ambient-weather-logger
```

Set it in cron and watch your data roll in.
```
> tail -f /var/log/syslog | grep CRON
May 11 16:15:01 media-center CRON[7793]: (austin) CMD (/opt/ambient-weather-logger/bin/ambient-weather-logger)
```

### Use the source, Luke!

Since you're not interested in using the prebuilt .deb, I'm going to assume 
you want to build for some other platform such as Winders or OSX. No worries, 
this is Java.

You'll need maven and Java 14 or later.

To build:
```
> mvn clean package
```

Once the build completes you can run like this:
```
> java -jar target/ambient-weather-logger-1.0.jar <options here>
```

There's a list of options, you can have a look at the Main class for a 
complete list but for quick reference here are the common ones.
```
-DappKey=<appKey>
-DapiKey=<apiKey>
-DmysqlHost=<host>
-DmysqlUserName=<user>
-DmysqlPassword=<pass>
```

### I want my own app.

If you'd rather write your own app you're in luck because there's a connector 
that makes that easy. Head over to 
[ambient-weather-java](https://github.com/rsv-code/ambient-weather-java).

### License
Copyright 2020 Austin Lehman. Licensed under the Apache License, Version 2.0