// SecureSocial worksheet
// Run with "mongo hfr securesocial.js"

// Users
db.users.remove()
db.users.insert({
    "id" : {
        "id" : "admin",
        "providerId" : "userpass"
    },
    "firstName" : "root",
    "lastName" : "",
    "fullName" : "",
    "email" : "any@tumblr.fr",
    "authMethod" : {
        "value" : "userPassword"
    },
    "passwordInfo" : {
        "hasher" : "bcrypt",
        "password" : "$2a$10$5ol6OmcFcmlky/obbawAHedzn92oXmAE9yNy/KWjiCvFNQ1iRDbUy"
    }
})
