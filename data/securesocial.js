// SecureSocial worksheet
// Run with "mongo hfr securesocial.js"

// Users
db.users.remove()
db.users.insert({
    "identityId" : {
        "userId" : "admin",
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
        "password" : "$2a$10$RDqo.MqxkD2n6uXC.TwCTuDHHvXA3ustOCA1uepBY6k980J74l3Y6"
    }
})
