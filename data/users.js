// Users worksheet
// Run with "mongo hfr users.js"

db.users.remove()
db.users.insert({
    "_id" : "admin",
    "password" : "$2a$10$zbOwsssQI2O7vD4Ux0HhYuX9mSFC6zc9ScWZ9vGg3nY5pTNqO88v2"
})
