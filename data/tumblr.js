// Tumblr worksheet
// Run with "mongo hfr tumblr.js"

// Site types
db.site_types.remove()
db.site_types.ensureIndex({ slug: 1 }, { unique: true })
db.site_types.insert([
    { "name": "Hfr", "slug": "hfr","ordinal": 1, "enabled": true },
    { "name": "Les joies", "slug": "les-joies","ordinal": 2, "enabled": true },
    { "name": "Divers","slug": "divers", "ordinal": 3, "enabled": true }
])

// Sites
db.sites.remove()
db.sites.ensureIndex({ slug: 1 }, { unique: true })
db.sites.insert(
    [
        {
            "siteType":{
                "_id":ObjectId("51e6fb7dcbd3092016a08fc6"),
                "name":"Hfr",
                "slug": "hfr",
                "ordinal":1,
                "enabled":true
            },
            "name":"Images étonnantes",
            "slug":"images-tonnantes",
            "url":"http://forum.hardware.fr/tumblr/Discussions/Loisirs/images-etonnantes-cons-sujet_78667_1.htm",
            "ordinal":1,
            "enabled":true,
            "configuration":{
                "cssSelectors":{
                    "images":{
                        "cssQuery":"tr.message td.messCase2 img",
                        "htmlAttribute":"src"
                    }
                },
                "lastPageByCss":true,
                "navigationAscending":false,
                "pageResolver":{
                    "pageNumberDescriptor":{
                        "cssSelector":{
                            "cssQuery":"tr.cBackHeader.fondForum2PagesHaut div.left a:last-child",
                            "htmlAttribute":"href"
                        },
                        "regex":"([0-9]+)\\.htm"
                    },
                    "changePageDescriptor":{
                        "regex":"_[0-9]+\\.",
                        "replacement":"_%d."
                    }
                },
                "imageRule":{
                    "exclude":"http://forum-images.hardware.fr/themes",
                    "startsWith":[
                        {
                            "value":"http://forum-images.hardware.fr/images/perso"
                        },
                        {
                            "value":"http://forum-images.hardware.fr/icones"
                        }
                    ]
                }
            }
        },
        {
            "siteType":{
                "_id":ObjectId("51e6fb7dcbd3092016a08fc6"),
                "name":"Hfr",
                "slug": "hfr",
                "ordinal":1,
                "enabled":true
            },
            "name":"Gifs: Femmes, Caca, Chutes&Co",
            "slug":"gifs-femmes-caca-chutesco",
            "url":"http://forum.hardware.fr/tumblr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_1.htm",
            "ordinal":2,
            "enabled":true,
            "configuration":{
                "cssSelectors":{
                    "images":{
                        "cssQuery":"tr.message td.messCase2 img",
                        "htmlAttribute":"src"
                    }
                },
                "lastPageByCss":true,
                "navigationAscending":false,
                "pageResolver":{
                    "pageNumberDescriptor":{
                        "cssSelector":{
                            "cssQuery":"tr.cBackHeader.fondForum2PagesHaut div.left a:last-child",
                            "htmlAttribute":"href"
                        },
                        "regex":"([0-9]+)\\.htm"
                    },
                    "changePageDescriptor":{
                        "regex":"_[0-9]+\\.",
                        "replacement":"_%d."
                    }
                },
                "imageRule":{
                    "exclude":"http://forum-images.hardware.fr/themes",
                    "startsWith":[
                        {
                            "value":"http://forum-images.hardware.fr/images/perso"
                        },
                        {
                            "value":"http://forum-images.hardware.fr/icones"
                        }
                    ]
                }
            }
        },
        {
            "siteType":{
                "_id":ObjectId("51e6fb7dcbd3092016a08fc7"),
                "name":"Les joies",
                "slug": "les-joies",
                "ordinal":2,
                "enabled":true
            },
            "name":"Joiesdusysadmin",
            "slug": "joiesdusyssadmin",
            "url":"http://lesjoiesdusysadmin.tumblr.com/page/1",
            "ordinal":3,
            "enabled": true,
            "configuration":{
                "cssSelectors":{
                    "images":{
                        "cssQuery":".post .bodytype img",
                        "htmlAttribute":"src"
                    },
                    "text":{
                        "cssQuery":".post h3 a",
                        "htmlAttribute":"href"
                    }
                },
                "lastPageByCss":true,
                "navigationAscending":true,
                "pageResolver":{
                    "pageNumberDescriptor":{
                        "cssSelector":{
                            "cssQuery":".pagination"
                        },
                        "regex":"([0-9]+)$"
                    },
                    "changePageDescriptor":{
                        "regex":"\\/page\\/[0-9]+",
                        "replacement":"/page/%d"
                    }
                }
            }
        },
        {
            "siteType":{
                "_id":ObjectId("51e6fb7dcbd3092016a08fc7"),
                "name":"Les joies","slug": "les-joies",
                "ordinal":2,
                "enabled":true
            },
            "name":"Joiesducode",
            "slug": "joiesducode",
            "url":"http://lesjoiesducode.tumblr.com/page/1",
            "ordinal":1,
            "enabled": true,
            "configuration":{
                "cssSelectors":{
                    "images":{
                        "cssQuery":".post .bodytype img",
                        "htmlAttribute":"src"
                    },
                    "text":{
                        "cssQuery":".post h3 a",
                        "htmlAttribute":"href"
                    }
                },
                "lastPageByCss":true,
                "navigationAscending":true,
                "pageResolver":{
                    "pageNumberDescriptor":{
                        "cssSelector":{
                            "cssQuery":".footer i"
                        },
                        "regex":"([0-9]+)$"
                    },
                    "changePageDescriptor":{
                        "regex":"\\/page\\/[0-9]+",
                        "replacement":"/page/%d"
                    }
                }
            }
        },
        {
            "siteType":{
                "_id":ObjectId("51e6fb7dcbd3092016a08fc7"),
                "name":"Les joies","slug": "les-joies",
                "ordinal":2,
                "enabled":true
            },
            "name":"Joiesdutest",
            "slug": "joiesdutest",
            "url":"http://lesjoiesdutest.tumblr.com/page/1",
            "ordinal":4,
            "enabled": true,
            "configuration":{
                "cssSelectors":{
                    "images":{
                        "cssQuery":".post p img",
                        "htmlAttribute":"src"
                    },
                    "text":{
                        "cssQuery":".post h3 a",
                        "htmlAttribute":"href"
                    }
                },
                "lastPageByCss":true,
                "navigationAscending":true,
                "pageResolver":{
                    "pageNumberDescriptor":{
                        "cssSelector":{
                            "cssQuery":".page-number"
                        },
                        "regex":"([0-9]+)$"
                    },
                    "changePageDescriptor":{
                        "regex":"\\/page\\/[0-9]+",
                        "replacement":"/page/%d"
                    }
                }
            }
        },
        {
            "siteType":{
                "_id":ObjectId("51e6fb7dcbd3092016a08fc7"),
                "name":"Les joies","slug": "les-joies",
                "ordinal":2,
                "enabled":true
            },
            "name":"Joiesduscrum",
            "slug": "joiesduscrum",
            "url":"http://lesjoiesduscrum.tumblr.com/page/1",
            "ordinal":2,
            "enabled": true,
            "configuration":{
                "cssSelectors":{
                    "images":{
                        "cssQuery":".post p img",
                        "htmlAttribute":"src"
                    },
                    "text":{
                        "cssQuery":".post a h3",
                        "htmlAttribute":"href"
                    }
                },
                "lastPageByCss":true,
                "navigationAscending":true,
                "pageResolver":{
                    "pageNumberDescriptor":{
                        "cssSelector":{
                            "cssQuery":"#prev-next"
                        },
                        "regex":"([0-9]+)$"
                    },
                    "changePageDescriptor":{
                        "regex":"\\/page\\/[0-9]+",
                        "replacement":"/page/%d"
                    }
                }
            }
        },
        {
            "siteType":{
                "_id":ObjectId("51e6fb7dcbd3092016a08fc7"),
                "name":"Divers",
                "slug": "divers",
                "ordinal":3,
                "enabled":true
            },
            "name":"Failbog.fr",
            "slug": "failblogfr",
            "url":"http://failblog.fr/fail/page-1.html",
            "ordinal":-1,
            "enabled": true,
            "configuration":{
                "cssSelectors":{
                    "images":{
                        "cssQuery":".contenu a img",
                        "htmlAttribute":"src"
                    },
                    "text":{
                        "cssQuery":".contenu h1 a"
                    }
                },
                "lastPageByCss":true,
                "navigationAscending":false,
                "pageResolver":{
                    "pageNumberDescriptor":{
                        "cssSelector":{
                            "cssQuery":".page p > a:last-child"
                        },
                        "regex":"([0-9]+)$"
                    },
                    "changePageDescriptor":{
                        "regex":"/page-[0-9]+.html",
                        "replacement":"/page-%d.html"
                    }
                }
            }
        },
        {
            "siteType":{
                "_id":ObjectId("51e6fb7dcbd3092016a08fc8"),
                "name":"Divers","slug": "divers",
                "ordinal":3,
                "enabled":true
            },
            "name":"ActressesWithoutTeeth",
            "slug": "actresseswithoutteeth",
            "url":"http://actresseswithoutteeth.net/page/1",
            "ordinal":-1,"enabled": true,
            "configuration":{
                "cssSelectors":{
                    "images":{
                        "cssQuery":".content .photo img",
                        "htmlAttribute":"src"
                    }
                },
                "lastPageByCss":false,
                "navigationAscending":true,
                "pageResolver":{
                    "pageNumberDescriptor":{
                        "cssSelector":{
                            "cssQuery":"a"
                        },
                        "regex":"([0-9]+)$"
                    },
                    "changePageDescriptor":{
                        "regex":"\\/page\\/[0-9]+",
                        "replacement":"/page/%d"
                    }
                }
            }
        },
        {
            "siteType":{
                "_id":ObjectId("51e6fb7dcbd3092016a08fc8"),
                "name":"Divers","slug": "divers",
                "ordinal":3,
                "enabled":true
            },
            "name":"ChersVoisins",
            "slug": "chersvoisins",
            "url":"http://chersvoisins.tumblr.com/page/1",
            "ordinal":-1,"enabled": true,
            "configuration":{
                "cssSelectors":{
                    "images":{
                        "cssQuery":".post img",
                        "htmlAttribute":"src"
                    }
                },
                "lastPageByCss":true,
                "navigationAscending":true,
                "pageResolver":{
                    "pageNumberDescriptor":{
                        "cssSelector":{
                            "cssQuery":"#page-location"
                        },
                        "regex":"([0-9]+)$"
                    },
                    "changePageDescriptor":{
                        "regex":"\\/page\\/[0-9]+",
                        "replacement":"/page/%d"
                    }
                }
            }
        },
        {
            "siteType":{
                "_id":ObjectId("51e6fb7dcbd3092016a08fc8"),
                "name":"Divers","slug": "divers",
                "ordinal":3,
                "enabled":true
            },
            "name":"CommitStrip",
            "slug": "commitstrip",
            "url":"http://www.commitstrip.com/page/1",
            "ordinal":-1,"enabled": true,
            "configuration":{
                "cssSelectors":{
                    "images":{
                        "cssQuery":".entry-content img",
                        "htmlAttribute":"src"
                    },
                    "text":{
                        "cssQuery":".post h1 a"
                    }
                },
                "lastPageByCss":false,
                "navigationAscending":true,
                "pageResolver":{
                    "pageNumberDescriptor":{
                        "cssSelector":{
                            "cssQuery":"a"
                        },
                        "regex":"([0-9]+)$"
                    },
                    "changePageDescriptor":{
                        "regex":"\\/page\\/[0-9]+",
                        "replacement":"/page/%d"
                    }
                }
            }
        },
        {
            "siteType":{
                "_id":ObjectId("51e6fb7dcbd3092016a08fc8"),
                "name":"Divers","slug": "divers",
                "ordinal":3,
                "enabled":true
            },
            "name":"DataAnxiety",
            "slug": "dataanxiety",
            "url":"http://dataanxiety.tumblr.com/page/1",
            "ordinal":-1,"enabled": true,
            "configuration":{
                "cssSelectors":{
                    "images":{
                        "cssQuery":".post-content img",
                        "htmlAttribute":"src"
                    }
                },
                "lastPageByCss":false,
                "navigationAscending":true,
                "pageResolver":{
                    "pageNumberDescriptor":{
                        "cssSelector":{
                            "cssQuery":"a"
                        },
                        "regex":"([0-9]+)$"
                    },
                    "changePageDescriptor":{
                        "regex":"\\/page\\/[0-9]+",
                        "replacement":"/page/%d"
                    }
                }
            }
        },
        {
            "siteType":{
                "_id":ObjectId("51e6fb7dcbd3092016a08fc8"),
                "name":"Divers","slug": "divers",
                "ordinal":3,
                "enabled":true
            },
            "name":"N'oubliez jamais la capote",
            "slug": "noubliez-jamais-la-capote",
            "url":"http://noubliezjamaislacapote.tumblr.com/page/1/",
            "ordinal":-1,"enabled": true,
            "configuration":{
                "cssSelectors":{
                    "images":{
                        "cssQuery":".post img",
                        "htmlAttribute":"src"
                    }
                },
                "lastPageByCss":true,
                "navigationAscending":true,
                "pageResolver":{
                    "pageNumberDescriptor":{
                        "cssSelector":{
                            "cssQuery":".pagination .count"
                        },
                        "regex":"([0-9]+)$"
                    },
                    "changePageDescriptor":{
                        "regex":"\\/page\\/[0-9]+",
                        "replacement":"/page/%d"
                    }
                }
            }
        },
        {
            "siteType":{
                "_id":ObjectId("51e6fb7dcbd3092016a08fc8"),
                "name":"Divers","slug": "divers",
                "ordinal":3,
                "enabled":true
            },
            "name":"Sportballsreplacedwithcats",
            "slug": "sportballsreplacedwithcats",
            "url":"http://sportballsreplacedwithcats.tumblr.com/page/1",
            "ordinal":-1,
            "enabled": true,
            "configuration":{
                "cssSelectors":{
                    "images":{
                        "cssQuery":".photo a img",
                        "htmlAttribute":"src"
                    },
                    "text":{
                        "cssQuery":".caption p:last-child"
                    }
                },
                "lastPageByCss":false,
                "navigationAscending":true,
                "pageResolver":{
                    "pageNumberDescriptor":{
                        "cssSelector":{
                            "cssQuery":"a"
                        },
                        "regex":"([0-9]+)$"
                    },
                    "changePageDescriptor":{
                        "regex":"\\/page\\/[0-9]+",
                        "replacement":"/page/%d"
                    }
                }
            }
        }
    ]
)

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
    },
    "_id" : ObjectId("51f3763b5bd11b010039f2cc")
})