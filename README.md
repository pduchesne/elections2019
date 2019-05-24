## Elections 2019 - Serving results data

### Prerequisite
 - Maven >3.0.0
 - JDK >= 8
 
### Java build
Let maven do the magic :
```
$> mvn clean package

```

### Running the server
Start the server with default values :
```
$> java -jar ./target/elections2019-0.0.1-jar-with-dependencies.jar 
```

Service can be accessed at http://localhost:8080 .
Example : http://localhost:8080/BR/K/21009 .

### Hot reload of files
Use `upload.sh` periodically (cron job) to continuously update data files from https://2019.elections.openknowledge.be/test. 
Zip files will be downloaded in `./zips` and expanded in `./data` as a flat directory. 
Only newer files will be downloaded and unzipped.

The server watches the file system and reloads automatically changed files.