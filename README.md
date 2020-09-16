###To run this project:

1. Create PostgreSql database.

⋅⋅* Change the user to postgres
 
`sudo -i -u postgres`

⋅⋅* Create User for Postgres

`createuser restuser`

⋅⋅* Create Database

`createdb restdb`

⋅⋅* Acces the postgres Shell

`psql`

⋅⋅* Provide the privileges to the postgres user

`alter user restuser with encrypted password 'restpassword';`

`grant all privileges on database restdb to restuser;`

2. Uncomment two commented lines in persistance.xml

3. Run App.main()

4. Comment those 2 lines back xD

5. Run server - jetty:run

####endpoints:

⋅⋅* /users

⋅⋅* /users/%id%

⋅⋅* /users/%id%/posts

⋅⋅* /posts

⋅⋅* /posts/%id%

⋅⋅* /posts/%id%/comments

⋅⋅* /locations/%id%

⋅⋅* /locations/%id%/posts

⋅⋅* /comments/%id%