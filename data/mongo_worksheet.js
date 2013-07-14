// Mongo worksheet
// Run with "mongo mongo_worksheet.js"

// Site types
db.site_types.ensureIndex( { name: 1 }, { unique: true } )
db.site_types.insert({"name": "Hfr"})
db.site_types.insert({"name": "Les joies"})
db.site_types.insert({"name": "Divers"})

// Sites
db.sites.ensureIndex( { siteType: 1, name: 1, url: 1 }, { unique: true } )
db.sites.insert({"siteType":"Hfr","name":"Images étonnantes","url":"http://forum.hardware.fr/tumblr/Discussions/Loisirs/images-etonnantes-cons-sujet_78667_1.htm","configuration":{"cssSelectors":{"images":{"cssQuery":"tr.message td.messCase2 img","htmlAttribute":"src"}},"lastPageByCss":true,"navigationAscending":false,"pageResolver":{"pageNumberDescriptor":{"cssSelector":{"cssQuery":"tr.cBackHeader.fondForum2PagesHaut div.left a:last-child","htmlAttribute":"href"},"regex":"([0-9]+)\\.htm"},"changePageDescriptor":{"regex":"_[0-9]+\\.","replacement":"_%d."}},"imageRule":{"exclude":"http://forum-images.hardware.fr/themes","startsWith":[{"value":"http://forum-images.hardware.fr/images/perso"},{"value":"http://forum-images.hardware.fr/icones"}]}}})

db.sites.insert({"siteType":"Hfr","name":"Gifs: Femmes, Caca, Chutes&Co","url":"http://forum.hardware.fr/tumblr/Discussions/Loisirs/chutes-warning-moderation-sujet_27848_1.htm","configuration":{"cssSelectors":{"images":{"cssQuery":"tr.message td.messCase2 img","htmlAttribute":"src"}},"lastPageByCss":true,"navigationAscending":false,"pageResolver":{"pageNumberDescriptor":{"cssSelector":{"cssQuery":"tr.cBackHeader.fondForum2PagesHaut div.left a:last-child","htmlAttribute":"href"},"regex":"([0-9]+)\\.htm"},"changePageDescriptor":{"regex":"_[0-9]+\\.","replacement":"_%d."}},"imageRule":{"exclude":"http://forum-images.hardware.fr/themes","startsWith":[{"value":"http://forum-images.hardware.fr/images/perso"},{"value":"http://forum-images.hardware.fr/icones"}]}}})

db.sites.insert({"siteType":"Les joies","name":"Joiesducode","url":"http://lesjoiesducode.tumblr.com/page/1","configuration":{"cssSelectors":{"images":{"cssQuery":".post .bodytype img","htmlAttribute":"src"},"text":{"cssQuery":".post h3 a","htmlAttribute":"href"}},"lastPageByCss":true,"navigationAscending":true,"pageResolver":{"pageNumberDescriptor":{"cssSelector":{"cssQuery":".footer i"},"regex":"([0-9]+)$"},"changePageDescriptor":{"regex":"\\/page\\/[0-9]+","replacement":"/page/%d"}}}})

db.sites.insert({"siteType":"Les joies","name":"Joiesdusysadmin","url":"http://lesjoiesdusysadmin.tumblr.com/page/1","configuration":{"cssSelectors":{"images":{"cssQuery":".post .bodytype img","htmlAttribute":"src"},"text":{"cssQuery":".post h3 a","htmlAttribute":"href"}},"lastPageByCss":true,"navigationAscending":true,"pageResolver":{"pageNumberDescriptor":{"cssSelector":{"cssQuery":".pagination"},"regex":"([0-9]+)$"},"changePageDescriptor":{"regex":"\\/page\\/[0-9]+","replacement":"/page/%d"}}}})

db.sites.insert({"siteType":"Les joies","name":"Joiesdutest","url":"http://lesjoiesdutest.tumblr.com/page/1","configuration":{"cssSelectors":{"images":{"cssQuery":".post p img","htmlAttribute":"src"},"text":{"cssQuery":".post h3 a","htmlAttribute":"href"}},"lastPageByCss":true,"navigationAscending":true,"pageResolver":{"pageNumberDescriptor":{"cssSelector":{"cssQuery":".page-number"},"regex":"([0-9]+)$"},"changePageDescriptor":{"regex":"\\/page\\/[0-9]+","replacement":"/page/%d"}}}})

db.sites.insert({"siteType":"Les joies","name":"Joiesduscrum","url":"http://lesjoiesduscrum.tumblr.com/page/1","configuration":{"cssSelectors":{"images":{"cssQuery":".post p img","htmlAttribute":"src"},"text":{"cssQuery":".post a h3","htmlAttribute":"href"}},"lastPageByCss":true,"navigationAscending":true,"pageResolver":{"pageNumberDescriptor":{"cssSelector":{"cssQuery":"#prev-next"},"regex":"([0-9]+)$"},"changePageDescriptor":{"regex":"\\/page\\/[0-9]+","replacement":"/page/%d"}}}})

db.sites.insert({"siteType":"Divers","name":"ActressesWithoutTeeth","url":"http://actresseswithoutteeth.net/page/1","configuration":{"cssSelectors":{"images":{"cssQuery":".content .photo img","htmlAttribute":"src"}},"lastPageByCss":false,"navigationAscending":true,"pageResolver":{"pageNumberDescriptor":{"cssSelector":{"cssQuery":"a"},"regex":"([0-9]+)$"},"changePageDescriptor":{"regex":"\\/page\\/[0-9]+","replacement":"/page/%d"}}}})

db.sites.insert({"siteType":"Divers","name":"ChersVoisins","url":"http://chersvoisins.tumblr.com/page/1","configuration":{"cssSelectors":{"images":{"cssQuery":".post img","htmlAttribute":"src"}},"lastPageByCss":true,"navigationAscending":true,"pageResolver":{"pageNumberDescriptor":{"cssSelector":{"cssQuery":"#page-location"},"regex":"([0-9]+)$"},"changePageDescriptor":{"regex":"\\/page\\/[0-9]+","replacement":"/page/%d"}}}})

db.sites.insert({"siteType":"Divers","name":"CommitStrip","url":"http://www.commitstrip.com/page/1","configuration":{"cssSelectors":{"images":{"cssQuery":".entry-content img","htmlAttribute":"src"},"text":{"cssQuery":".post h1 a"}},"lastPageByCss":false,"navigationAscending":true,"pageResolver":{"pageNumberDescriptor":{"cssSelector":{"cssQuery":"a"},"regex":"([0-9]+)$"},"changePageDescriptor":{"regex":"\\/page\\/[0-9]+","replacement":"/page/%d"}}}})

db.sites.insert({"siteType":"Divers","name":"DataAnxiety","url":"http://dataanxiety.tumblr.com/page/1","configuration":{"cssSelectors":{"images":{"cssQuery":".post-content img","htmlAttribute":"src"}},"lastPageByCss":false,"navigationAscending":true,"pageResolver":{"pageNumberDescriptor":{"cssSelector":{"cssQuery":"a"},"regex":"([0-9]+)$"},"changePageDescriptor":{"regex":"\\/page\\/[0-9]+","replacement":"/page/%d"}}}})

db.sites.insert({"siteType":"Divers","name":"Failbog.fr","url":"http://failblog.fr/fail/page-1.html","configuration":{"cssSelectors":{"images":{"cssQuery":".contenu a img","htmlAttribute":"src"},"text":{"cssQuery":".contenu h1 a"}},"lastPageByCss":true,"navigationAscending":true,"pageResolver":{"pageNumberDescriptor":{"cssSelector":{"cssQuery":".page p > a:last-child"},"regex":"([0-9]+)$"},"changePageDescriptor":{"regex":"/page-[0-9]+.html","replacement":"/page-%d.html"}}}})

db.sites.insert({"siteType":"Divers","name":"N'oubliez jamais la capote","url":"http://noubliezjamaislacapote.tumblr.com/page/1/","configuration":{"cssSelectors":{"images":{"cssQuery":".post img","htmlAttribute":"src"}},"lastPageByCss":true,"navigationAscending":true,"pageResolver":{"pageNumberDescriptor":{"cssSelector":{"cssQuery":".pagination .count"},"regex":"([0-9]+)$"},"changePageDescriptor":{"regex":"\\/page\\/[0-9]+","replacement":"/page/%d"}}}})

db.sites.insert({"siteType":"Divers","name":"Sportballsreplacedwithcats","url":"http://sportballsreplacedwithcats.tumblr.com/page/1","configuration":{"cssSelectors":{"images":{"cssQuery":".photo_post img","htmlAttribute":"src"}},"lastPageByCss":false,"navigationAscending":true,"pageResolver":{"pageNumberDescriptor":{"cssSelector":{"cssQuery":"a"},"regex":"([0-9]+)$"},"changePageDescriptor":{"regex":"\\/page\\/[0-9]+","replacement":"/page/%d"}}}})