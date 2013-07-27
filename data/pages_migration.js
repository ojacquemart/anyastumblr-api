// Migration worksheet
// Run with mongo hfr pages_migration.js

// Stats by site.
db.pages.aggregate([
    { $group: { _id: { siteId: "$siteId" }, number: { $sum: 1 } } },
    { $sort: { "number": -1 } }
]);

// Remove orphelan pages.
db.pages.remove({siteId: "B1ooWWP0Nghya3RECFcH46Uwe4="})
db.pages.remove({siteId: "AHBLjHkTIxuUwJePXAYQVkmkpBE="})
db.pages.remove({siteId: "YOJPakDQEV34qWyQZgAqKk5Bvok="})
db.pages.remove({siteId: "DYGdUtldtfcc8JtBzaLtIq1Xu4="})

// Replace old sites id by slugs.

// Images étonnantes
db.pages.update(
    { siteId: "uVK0Az5Jyjyxh67RyRwcUIYtc=" },
    {
        $set: { 'siteId': 'images-tonnantes' }
    },
    {
        multi: true
    }
)

// Gifs: Femmes, Caca, Chutes&Co
db.pages.update(
    {
        siteId: "WZmDr4ScGyXLBTajrtbHZ7V4=" },
    {
        $set: { 'siteId': 'gifs-femmes-caca-chutesco' }
    },
    {
        multi: true
    }
)

// Joiesducode
db.pages.update(
    {
        siteId: "Ts2uN2oIx4Pyk6NCh6kjHizvEl0=" },
    {
        $set: { 'siteId': 'joiesducode' }
    },
    {
        multi: true
    }
)

// Joiesdusysadmin
db.pages.update(
    {
        siteId: "ZZKVfOMuLSvDUCQvotlVaTQSXc=" },
    {
        $set: { 'siteId': 'joiesdusyssadmin' }
    },
    {
        multi: true
    }
)

// Joiesdutest
db.pages.update(
    {
        siteId: "FywayWKJZhewL5pIGb86IjfYJQ4=" },
    {
        $set: { 'siteId': 'joiesdutest' }
    },
    {
        multi: true
    }
)

// Joiesduscrum
db.pages.update(
    {
        siteId: "7sg8qRPhlAWiIwOjg56ME5B6Uc=" },
    {
        $set: { 'siteId': 'joiesduscrum' }
    },
    {
        multi: true
    }
)

// ActressesWithoutTeeth
db.pages.update(
    {
        siteId: "xFqo2nJb2GaNZxxZnKm7R6Ul8g=" },
    {
        $set: { 'siteId': 'actresseswithoutteeth' }
    },
    {
        multi: true
    }
)

// ChersVoisins
db.pages.update(
    {
        siteId: "6fiY5IQIvRxYhWiJtxUzk6YWqhg=" },
    {
        $set: { 'siteId': 'chersvoisins' }
    },
    {
        multi: true
    }
)

// CommitStrip
db.pages.update(
    {
        siteId: "rgy8dkKWcCUUSwUCO0k9mvdL5w=" },
    {
        $set: { 'siteId': 'commitstrip' }
    },
    {
        multi: true
    }
)

// DataAnxiety
db.pages.update(
    {
        siteId: "RvBkJOcoCfeqCZiGiw25y3p4Vog=" },
    {
        $set: { 'siteId': 'dataanxiety' }
    },
    {
        multi: true
    }
)

// Failbog.fr
db.pages.update(
    {
        siteId: "N8HXKR1MIW0ZrPUXfsvN5QdoIg=" },
    {
        $set: { 'siteId': 'failblog' }
    },
    {
        multi: true
    }
)

// N'oubliez jamais la capote
db.pages.update(
    {
        siteId: "jNx66g8FLSwsHMsGZcyMSN81xs=" },
    {
        $set: { 'siteId': 'noubliez-jamais-la-capote' }
    },
    {
        multi: true
    }
)

// Sportballsreplacedwithcats
db.pages.update(
    {
        siteId: "n9z5AGT0dfG0R1isr7RYSjfTqf0=" },
    {
        $set: { 'siteId': 'sportballsreplacedwithcats' }
    },
    {
        multi: true
    }
)
