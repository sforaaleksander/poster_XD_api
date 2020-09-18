#!/bin/bash

echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<persistence xmlns=\"http://xmlns.jcp.org/xml/ns/persistence\"
             xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"
             xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd\"
             version=\"2.1\">

    <persistence-unit name=\"posterPU\" transaction-type=\"RESOURCE_LOCAL\">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        <class>com.codecool.poster_xd_api.models.User</class>
        <class>com.codecool.poster_xd_api.models.Post</class>
        <class>com.codecool.poster_xd_api.models.Location</class>
        <class>com.codecool.poster_xd_api.models.Comment</class>
        <properties>
            <property name=\"hibernate.dialect\" value=\"org.hibernate.dialect.PostgreSQL94Dialect\"/>
            <property name=\"hibernate.hbm2dll.auto\" value=\"create-drop\"/>
            <property name=\"hibernate.show_sql\" value=\"true\"/>
            <property name=\"hibernate.format_sql\" value=\"true\"/>
            <property name=\"hibernate.use_sql_comments\" value=\"true\"/>

            <property name=\"javax.persistence.schema-generation.database.action\" value=\"drop-and-create\"/>
            <property name=\"javax.persistence.schema-generation.scripts.action\" value=\"drop-and-create\"/>

            <property name=\"javax.persistence.schema-generation.scripts.create-target\"
                      value=\"src/main/resources/META-INF/create-schema.ddl\"/>
            <property name=\"javax.persistence.schema-generation.scripts.drop-target\"
                      value=\"src/main/resources/META-INF/drop-schema.ddl\"/>

            <property name=\"javax.persistence.jdbc.driver\" value=\"org.postgresql.Driver\"/>
            <property name=\"javax.persistence.jdbc.url\" value=\"jdbc:postgresql://localhost:5432/restdb\"/>
            <property name=\"javax.persistence.jdbc.user\" value=\"restuser\"/>
            <property name=\"javax.persistence.jdbc.password\" value=\"restpassword\"/>
        </properties>
    </persistence-unit>
</persistence>" > src/main/resources/META-INF/persistence.xml

echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>

<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"
         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.codecool.poster_xd_api</groupId>
    <artifactId>poster_xd_api</artifactId>
    <version>1.0-SNAPSHOT</version>

    <name>poster_xd_api</name>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>jakarta.xml.bind</groupId>
            <artifactId>jakarta.xml.bind-api</artifactId>
            <version>2.3.2</version>
        </dependency>

        <dependency>
            <groupId>org.glassfish.jaxb</groupId>
            <artifactId>jaxb-runtime</artifactId>
            <version>2.3.2</version>
        </dependency>

        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.0</version>
            <scope>provided</scope>
        </dependency>

        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
            <version>5.2.10.Final</version>
        </dependency>

        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.1.1</version>
        </dependency>
      
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.8.6</version>
        </dependency>
    </dependencies>

    <build>
        <finalName>poster_xd_api</finalName>
        <sourceDirectory>src/main/java</sourceDirectory>

        <plugins>
            <plugin>
                <groupId>org.eclipse.jetty</groupId>
                <artifactId>jetty-maven-plugin</artifactId>
                <version>9.4.9.v20180320</version>
                <configuration>
                    <reload>automatic</reload>
                    <scanIntervalSeconds>1</scanIntervalSeconds>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.2.0</version>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.7.0</version>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.2.4</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <transformers>
                                <transformer implementation=\"org.apache.maven.plugins.shade.resource.ManifestResourceTransformer\">
                                    <mainClass>com.codecool.poster_xd_api.App</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>" > pom.xml

mvn clean install -U

mvn package

java -jar target/poster_xd_api.jar

echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<persistence xmlns=\"http://xmlns.jcp.org/xml/ns/persistence\"
             xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"
             xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd\"
             version=\"2.1\">

    <persistence-unit name=\"posterPU\" transaction-type=\"RESOURCE_LOCAL\">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        <class>com.codecool.poster_xd_api.models.User</class>
        <class>com.codecool.poster_xd_api.models.Post</class>
        <class>com.codecool.poster_xd_api.models.Location</class>
        <class>com.codecool.poster_xd_api.models.Comment</class>
        <properties>
            <property name=\"hibernate.dialect\" value=\"org.hibernate.dialect.PostgreSQL94Dialect\"/>
            <property name=\"hibernate.hbm2dll.auto\" value=\"create-drop\"/>
            <property name=\"hibernate.show_sql\" value=\"true\"/>
            <property name=\"hibernate.format_sql\" value=\"true\"/>
            <property name=\"hibernate.use_sql_comments\" value=\"true\"/>

<!--            <property name=\"javax.persistence.schema-generation.database.action\" value=\"drop-and-create\"/>-->
<!--            <property name=\"javax.persistence.schema-generation.scripts.action\" value=\"drop-and-create\"/>-->

            <property name=\"javax.persistence.schema-generation.scripts.create-target\"
                      value=\"src/main/resources/META-INF/create-schema.ddl\"/>
            <property name=\"javax.persistence.schema-generation.scripts.drop-target\"
                      value=\"src/main/resources/META-INF/drop-schema.ddl\"/>

            <property name=\"javax.persistence.jdbc.driver\" value=\"org.postgresql.Driver\"/>
            <property name=\"javax.persistence.jdbc.url\" value=\"jdbc:postgresql://localhost:5432/restdb\"/>
            <property name=\"javax.persistence.jdbc.user\" value=\"restuser\"/>
            <property name=\"javax.persistence.jdbc.password\" value=\"restpassword\"/>
        </properties>
    </persistence-unit>
</persistence>" > src/main/resources/META-INF/persistence.xml

echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>

<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"
         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.codecool.poster_xd_api</groupId>
    <artifactId>poster_xd_api</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <name>poster_xd_api</name>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>jakarta.xml.bind</groupId>
            <artifactId>jakarta.xml.bind-api</artifactId>
            <version>2.3.2</version>
        </dependency>

        <dependency>
            <groupId>org.glassfish.jaxb</groupId>
            <artifactId>jaxb-runtime</artifactId>
            <version>2.3.2</version>
        </dependency>

        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.0</version>
            <scope>provided</scope>
        </dependency>

        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
            <version>5.2.10.Final</version>
        </dependency>

        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.1.1</version>
        </dependency>
      
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.8.6</version>
        </dependency>
    </dependencies>

    <build>
        <finalName>poster_xd_api</finalName>
        <sourceDirectory>src/main/java</sourceDirectory>

        <plugins>
            <plugin>
                <groupId>org.eclipse.jetty</groupId>
                <artifactId>jetty-maven-plugin</artifactId>
                <version>9.4.9.v20180320</version>
                <configuration>
                    <reload>automatic</reload>
                    <scanIntervalSeconds>1</scanIntervalSeconds>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.2.0</version>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.7.0</version>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>" > pom.xml

mvn clean install -U

mvn jetty:run
