
### To run this project:

1. Create PostgresSql database.

* Change the user to postgres
 
`sudo -i -u postgres`

* Create User for Postgres

`createuser restuser`

* Create Database

`createdb restdb`

* Access the postgres Shell

`psql`

* Provide the privileges to the postgres user

`alter user restuser with encrypted password 'restpassword';`

`grant all privileges on database restdb to restuser;`

2. Run script run.sh in terminal (you have to be in the project directory).

`bash run.sh`

#### endpoints:

* /users
* /users/%id%
* /users/%id%/posts
* /posts
* /posts/%id%
* /posts/%id%/comments
* /locations/%id%
* /locations/%id%/posts
* /comments/%id%
