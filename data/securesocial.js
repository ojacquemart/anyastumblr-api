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
        "password" : "$2a$10$J2MsM.fIYLK5.JJ.Xz3/wemmY6adsRcYlTs7EK5vXY4Ks7cKpwZNW"
    }
})
