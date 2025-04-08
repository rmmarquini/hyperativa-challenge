// Connect to the 'hyperativa_challenge' database to ensure it is created
db = connect("hyperativa_challenge");

// Create the 'appdev' user in the 'admin' database with permissions for 'hyperativa_challenge'
db.createUser({
  user: "appdev",
  pwd: "asdasdasd",
  roles: [
    { role: "readWrite", db: "hyperativa_challenge" }
  ]
});

// Create cards collection to ensure the database is persisted
db.createCollection("cards");

