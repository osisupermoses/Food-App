package com.osisupermoses.healthbloc.presentation.screens.register_user_screen.components

import java.util.*
import android.telephony.TelephonyManager

data class Country(
    var nameCode: String = "",
    var code: String = "",
    val fullName: String = ""
)

fun getFlagEmojiFor(countryCode: String): String {
    return when (countryCode.lowercase()) {
        "ad" -> "🇦🇩"
        "ae" -> "🇦🇪"
        "af" -> "🇦🇫"
        "ag" -> "🇦🇬"
        "ai" -> "🇦🇮"
        "al" -> "🇦🇱"
        "am" -> "🇦🇲"
        "ao" -> "🇦🇴"
        "aq" -> "🇦🇶"
        "ar" -> "🇦🇷"
        "as" -> "🇦🇸"
        "at" -> "🇦🇹"
        "au" -> "🇦🇺"
        "aw" -> "🇦🇼"
        "ax" -> "🇦🇽"
        "az" -> "🇦🇿"
        "ba" -> "🇧🇦"
        "bb" -> "🇧🇧"
        "bd" -> "🇧🇩"
        "be" -> "🇧🇪"
        "bf" -> "🇧🇫"
        "bg" -> "🇧🇬"
        "bh" -> "🇧🇭"
        "bi" -> "🇧🇮"
        "bj" -> "🇧🇯"
        "bl" -> "🇧🇱"
        "bm" -> "🇧🇲"
        "bn" -> "🇧🇳"
        "bo" -> "🇧🇴"
        "bq" -> "🇧🇶"
        "br" -> "🇧🇷"
        "bs" -> "🇧🇸"
        "bt" -> "🇧🇹"
        "bv" -> "🇧🇻"
        "bw" -> "🇧🇼"
        "by" -> "🇧🇾"
        "bz" -> "🇧🇿"
        "ca" -> "🇨🇦"
        "cc" -> "🇨🇨"
        "cd" -> "🇨🇩"
        "cf" -> "🇨🇫"
        "cg" -> "🇨🇬"
        "ch" -> "🇨🇭"
        "ci" -> "🇨🇮"
        "ck" -> "🇨🇰"
        "cl" -> "🇨🇱"
        "cm" -> "🇨🇲"
        "cn" -> "🇨🇳"
        "co" -> "🇨🇴"
        "cr" -> "🇨🇷"
        "cu" -> "🇨🇺"
        "cv" -> "🇨🇻"
        "cw" -> "🇨🇼"
        "cx" -> "🇨🇽"
        "cy" -> "🇨🇾"
        "cz" -> "🇨🇿"
        "de" -> "🇩🇪"
        "dj" -> "🇩🇯"
        "dk" -> "🇩🇰"
        "dm" -> "🇩🇲"
        "do" -> "🇩🇴"
        "dz" -> "🇩🇿"
        "ec" -> "🇪🇨"
        "ee" -> "🇪🇪"
        "eg" -> "🇪🇬"
        "eh" -> "🇪🇭"
        "er" -> "🇪🇷"
        "es" -> "🇪🇸"
        "et" -> "🇪🇹"
        "fi" -> "🇫🇮"
        "fj" -> "🇫🇯"
        "fk" -> "🇫🇰"
        "fm" -> "🇫🇲"
        "fo" -> "🇫🇴"
        "fr" -> "🇫🇷"
        "ga" -> "🇬🇦"
        "gb" -> "🇬🇧"
        "gd" -> "🇬🇩"
        "ge" -> "🇬🇪"
        "gf" -> "🇬🇫"
        "gg" -> "🇬🇬"
        "gh" -> "🇬🇭"
        "gi" -> "🇬🇮"
        "gl" -> "🇬🇱"
        "gm" -> "🇬🇲"
        "gn" -> "🇬🇳"
        "gp" -> "🇬🇵"
        "gq" -> "🇬🇶"
        "gr" -> "🇬🇷"
        "gs" -> "🇬🇸"
        "gt" -> "🇬🇹"
        "gu" -> "🇬🇺"
        "gw" -> "🇬🇼"
        "gy" -> "🇬🇾"
        "hk" -> "🇭🇰"
        "hm" -> "🇭🇲"
        "hn" -> "🇭🇳"
        "hr" -> "🇭🇷"
        "ht" -> "🇭🇹"
        "hu" -> "🇭🇺"
        "id" -> "🇮🇩"
        "ie" -> "🇮🇪"
        "il" -> "🇮🇱"
        "im" -> "🇮🇲"
        "in" -> "🇮🇳"
        "io" -> "🇮🇴"
        "iq" -> "🇮🇶"
        "ir" -> "🇮🇷"
        "is" -> "🇮🇸"
        "it" -> "🇮🇹"
        "je" -> "🇯🇪"
        "jm" -> "🇯🇲"
        "jo" -> "🇯🇴"
        "jp" -> "🇯🇵"
        "ke" -> "🇰🇪"
        "kg" -> "🇰🇬"
        "kh" -> "🇰🇭"
        "ki" -> "🇰🇮"
        "km" -> "🇰🇲"
        "kn" -> "🇰🇳"
        "kp" -> "🇰🇵"
        "kr" -> "🇰🇷"
        "kw" -> "🇰🇼"
        "ky" -> "🇰🇾"
        "kz" -> "🇰🇿"
        "la" -> "🇱🇦"
        "lb" -> "🇱🇧"
        "lc" -> "🇱🇨"
        "li" -> "🇱🇮"
        "lk" -> "🇱🇰"
        "lr" -> "🇱🇷"
        "ls" -> "🇱🇸"
        "lt" -> "🇱🇹"
        "lu" -> "🇱🇺"
        "lv" -> "🇱🇻"
        "ly" -> "🇱🇾"
        "ma" -> "🇲🇦"
        "mc" -> "🇲🇨"
        "md" -> "🇲🇩"
        "me" -> "🇲🇪"
        "mf" -> "🇲🇫"
        "mg" -> "🇲🇬"
        "mh" -> "🇲🇭"
        "mk" -> "🇲🇰"
        "ml" -> "🇲🇱"
        "mm" -> "🇲🇲"
        "mn" -> "🇲🇳"
        "mo" -> "🇲🇴"
        "mp" -> "🇲🇵"
        "mq" -> "🇲🇶"
        "mr" -> "🇲🇷"
        "ms" -> "🇲🇸"
        "mt" -> "🇲🇹"
        "mu" -> "🇲🇺"
        "mv" -> "🇲🇻"
        "mw" -> "🇲🇼"
        "mx" -> "🇲🇽"
        "my" -> "🇲🇾"
        "mz" -> "🇲🇿"
        "na" -> "🇳🇦"
        "nc" -> "🇳🇨"
        "ne" -> "🇳🇪"
        "nf" -> "🇳🇫"
        "ng" -> "🇳🇬"
        "ni" -> "🇳🇮"
        "nl" -> "🇳🇱"
        "no" -> "🇳🇴"
        "np" -> "🇳🇵"
        "nr" -> "🇳🇷"
        "nu" -> "🇳🇺"
        "nz" -> "🇳🇿"
        "om" -> "🇴🇲"
        "pa" -> "🇵🇦"
        "pe" -> "🇵🇪"
        "pf" -> "🇵🇫"
        "pg" -> "🇵🇬"
        "ph" -> "🇵🇭"
        "pk" -> "🇵🇰"
        "pl" -> "🇵🇱"
        "pm" -> "🇵🇲"
        "pn" -> "🇵🇳"
        "pr" -> "🇵🇷"
        "ps" -> "🇵🇸"
        "pt" -> "🇵🇹"
        "pw" -> "🇵🇼"
        "py" -> "🇵🇾"
        "qa" -> "🇶🇦"
        "re" -> "🇷🇪"
        "ro" -> "🇷🇴"
        "rs" -> "🇷🇸"
        "ru" -> "🇷🇺"
        "rw" -> "🇷🇼"
        "sa" -> "🇸🇦"
        "sb" -> "🇸🇧"
        "sc" -> "🇸🇨"
        "sd" -> "🇸🇩"
        "se" -> "🇸🇪"
        "sg" -> "🇸🇬"
        "sh" -> "🇸🇭"
        "si" -> "🇸🇮"
        "sj" -> "🇸🇯"
        "sk" -> "🇸🇰"
        "sl" -> "🇸🇱"
        "sm" -> "🇸🇲"
        "sn" -> "🇸🇳"
        "so" -> "🇸🇴"
        "sr" -> "🇸🇷"
        "ss" -> "🇸🇸"
        "st" -> "🇸🇹"
        "sv" -> "🇸🇻"
        "sx" -> "🇸🇽"
        "sy" -> "🇸🇾"
        "sz" -> "🇸🇿"
        "tc" -> "🇹🇨"
        "td" -> "🇹🇩"
        "tf" -> "🇹🇫"
        "tg" -> "🇹🇬"
        "th" -> "🇹🇭"
        "tj" -> "🇹🇯"
        "tk" -> "🇹🇰"
        "tl" -> "🇹🇱"
        "tm" -> "🇹🇲"
        "tn" -> "🇹🇳"
        "to" -> "🇹🇴"
        "tr" -> "🇹🇷"
        "tt" -> "🇹🇹"
        "tv" -> "🇹🇻"
        "tw" -> "🇹🇼"
        "tz" -> "🇹🇿"
        "ua" -> "🇺🇦"
        "ug" -> "🇺🇬"
        "um" -> "🇺🇲"
        "us" -> "🇺🇸"
        "uy" -> "🇺🇾"
        "uz" -> "🇺🇿"
        "va" -> "🇻🇦"
        "vc" -> "🇻🇨"
        "ve" -> "🇻🇪"
        "vg" -> "🇻🇬"
        "vi" -> "🇻🇮"
        "vn" -> "🇻🇳"
        "vu" -> "🇻🇺"
        "wf" -> "🇼🇫"
        "ws" -> "🇼🇸"
        "xk" -> "🇽🇰"
        "ye" -> "🇾🇪"
        "yt" -> "🇾🇹"
        "za" -> "🇿🇦"
        "zm" -> "🇿🇲"
        "zw" -> "🇿🇼"
        else -> " "
    }
}

fun getCountriesList(): List<Country> {
    val countries = mutableListOf<Country>()
    countries.add(Country("ad", "376", "Andorra"))
    countries.add(Country("ae", "971", "United Arab Emirates (UAE))"))
    countries.add(Country("af", "93", "Afghanistan"))
    countries.add(Country("ag", "1", "Antigua and Barbuda"))
    countries.add(Country("ai", "1", "Anguilla"))
    countries.add(Country("al", "355", "Albania"))
    countries.add(Country("am", "374", "Armenia"))
    countries.add(Country("ao", "244", "Angola"))
    countries.add(Country("aq", "672", "Antarctica"))
    countries.add(Country("ar", "54", "Argentina"))
    countries.add(Country("as", "1", "American Samoa"))
    countries.add(Country("at", "43", "Austria"))
    countries.add(Country("au", "61", "Australia"))
    countries.add(Country("aw", "297", "Aruba"))
    countries.add(Country("ax", "358", "Åland Islands"))
    countries.add(Country("az", "994", "Azerbaijan"))
    countries.add(Country("ba", "387", "Bosnia And Herzegovina"))
    countries.add(Country("bb", "1", "Barbados"))
    countries.add(Country("bd", "880", "Bangladesh"))
    countries.add(Country("be", "32", "Belgium"))
    countries.add(Country("bf", "226", "Burkina Faso"))
    countries.add(Country("bg", "359", "Bulgaria"))
    countries.add(Country("bh", "973", "Bahrain"))
    countries.add(Country("bi", "257", "Burundi"))
    countries.add(Country("bj", "229", "Benin"))
    countries.add(Country("bl", "590", "Saint Barthélemy"))
    countries.add(Country("bm", "1", "Bermuda"))
    countries.add(Country("bn", "673", "Brunei Darussalam"))
    countries.add(Country("bo", "591", "Bolivia, Plurinational State Of"))
    countries.add(Country("br", "55", "Brazil"))
    countries.add(Country("bs", "1", "Bahamas"))
    countries.add(Country("bt", "975", "Bhutan"))
    countries.add(Country("bw", "267", "Botswana"))
    countries.add(Country("by", "375", "Belarus"))
    countries.add(Country("bz", "501", "Belize"))
    countries.add(Country("ca", "1", "Canada"))
    countries.add(Country("cc", "61", "Cocos (keeling)) Islands"))
    countries.add(Country("cd", "243", "Congo, The Democratic Republic Of The"))
    countries.add(Country("cf", "236", "Central African Republic"))
    countries.add(Country("cg", "242", "Congo"))
    countries.add(Country("ch", "41", "Switzerland"))
    countries.add(Country("ci", "225", "Côte D'ivoire"))
    countries.add(Country("ck", "682", "Cook Islands"))
    countries.add(Country("cl", "56", "Chile"))
    countries.add(Country("cm", "237", "Cameroon"))
    countries.add(Country("cn", "86", "China"))
    countries.add(Country("co", "57", "Colombia"))
    countries.add(Country("cr", "506", "Costa Rica"))
    countries.add(Country("cu", "53", "Cuba"))
    countries.add(Country("cv", "238", "Cape Verde"))
    countries.add(Country("cw", "599", "Curaçao"))
    countries.add(Country("cx", "61", "Christmas Island"))
    countries.add(Country("cy", "357", "Cyprus"))
    countries.add(Country("cz", "420", "Czech Republic"))
    countries.add(Country("de", "49", "Germany"))
    countries.add(Country("dj", "253", "Djibouti"))
    countries.add(Country("dk", "45", "Denmark"))
    countries.add(Country("dm", "1", "Dominica"))
    countries.add(Country("do", "1", "Dominican Republic"))
    countries.add(Country("dz", "213", "Algeria"))
    countries.add(Country("ec", "593", "Ecuador"))
    countries.add(Country("ee", "372", "Estonia"))
    countries.add(Country("eg", "20", "Egypt"))
    countries.add(Country("er", "291", "Eritrea"))
    countries.add(Country("es", "34", "Spain"))
    countries.add(Country("et", "251", "Ethiopia"))
    countries.add(Country("fi", "358", "Finland"))
    countries.add(Country("fj", "679", "Fiji"))
    countries.add(Country("fk", "500", "Falkland Islands (malvinas))"))
    countries.add(Country("fm", "691", "Micronesia, Federated States Of"))
    countries.add(Country("fo", "298", "Faroe Islands"))
    countries.add(Country("fr", "33", "France"))
    countries.add(Country("ga", "241", "Gabon"))
    countries.add(Country("gb", "44", "United Kingdom"))
    countries.add(Country("gd", "1", "Grenada"))
    countries.add(Country("ge", "995", "Georgia"))
    countries.add(Country("gf", "594", "French Guyana"))
    countries.add(Country("gh", "233", "Ghana"))
    countries.add(Country("gi", "350", "Gibraltar"))
    countries.add(Country("gl", "299", "Greenland"))
    countries.add(Country("gm", "220", "Gambia"))
    countries.add(Country("gn", "224", "Guinea"))
    countries.add(Country("gp", "450", "Guadeloupe"))
    countries.add(Country("gq", "240", "Equatorial Guinea"))
    countries.add(Country("gr", "30", "Greece"))
    countries.add(Country("gt", "502", "Guatemala"))
    countries.add(Country("gu", "1", "Guam"))
    countries.add(Country("gw", "245", "Guinea-bissau"))
    countries.add(Country("gy", "592", "Guyana"))
    countries.add(Country("hk", "852", "Hong Kong"))
    countries.add(Country("hn", "504", "Honduras"))
    countries.add(Country("hr", "385", "Croatia"))
    countries.add(Country("ht", "509", "Haiti"))
    countries.add(Country("hu", "36", "Hungary"))
    countries.add(Country("id", "62", "Indonesia"))
    countries.add(Country("ie", "353", "Ireland"))
    countries.add(Country("il", "972", "Israel"))
    countries.add(Country("im", "44", "Isle Of Man"))
    countries.add(Country("is", "354", "Iceland"))
    countries.add(Country("in", "91", "India"))
    countries.add(Country("io", "246", "British Indian Ocean Territory"))
    countries.add(Country("iq", "964", "Iraq"))
    countries.add(Country("ir", "98", "Iran, Islamic Republic Of"))
    countries.add(Country("it", "39", "Italy"))
    countries.add(Country("je", "44", "Jersey "))
    countries.add(Country("jm", "1", "Jamaica"))
    countries.add(Country("jo", "962", "Jordan"))
    countries.add(Country("jp", "81", "Japan"))
    countries.add(Country("ke", "254", "Kenya"))
    countries.add(Country("kg", "996", "Kyrgyzstan"))
    countries.add(Country("kh", "855", "Cambodia"))
    countries.add(Country("ki", "686", "Kiribati"))
    countries.add(Country("km", "269", "Comoros"))
    countries.add(Country("kn", "1", "Saint Kitts and Nevis"))
    countries.add(Country("kp", "850", "North Korea"))
    countries.add(Country("kr", "82", "South Korea"))
    countries.add(Country("kw", "965", "Kuwait"))
    countries.add(Country("ky", "1", "Cayman Islands"))
    countries.add(Country("kz", "7", "Kazakhstan"))
    countries.add(Country("la", "856", "Lao People's Democratic Republic"))
    countries.add(Country("lb", "961", "Lebanon"))
    countries.add(Country("lc", "1", "Saint Lucia"))
    countries.add(Country("li", "423", "Liechtenstein"))
    countries.add(Country("lk", "94", "Sri Lanka"))
    countries.add(Country("lr", "231", "Liberia"))
    countries.add(Country("ls", "266", "Lesotho"))
    countries.add(Country("lt", "370", "Lithuania"))
    countries.add(Country("lu", "352", "Luxembourg"))
    countries.add(Country("lv", "371", "Latvia"))
    countries.add(Country("ly", "218", "Libya"))
    countries.add(Country("ma", "212", "Morocco"))
    countries.add(Country("mc", "377", "Monaco"))
    countries.add(Country("md", "373", "Moldova, Republic Of"))
    countries.add(Country("me", "382", "Montenegro"))
    countries.add(Country("mf", "590", "Saint Martin"))
    countries.add(Country("mg", "261", "Madagascar"))
    countries.add(Country("mh", "692", "Marshall Islands"))
    countries.add(Country("mk", "389", "Macedonia (FYROM))"))
    countries.add(Country("ml", "223", "Mali"))
    countries.add(Country("mm", "95", "Myanmar"))
    countries.add(Country("mn", "976", "Mongolia"))
    countries.add(Country("mo", "853", "Macau"))
    countries.add(Country("mp", "1", "Northern Mariana Islands"))
    countries.add(Country("mq", "596", "Martinique"))
    countries.add(Country("mr", "222", "Mauritania"))
    countries.add(Country("ms", "1", "Montserrat"))
    countries.add(Country("mt", "356", "Malta"))
    countries.add(Country("mu", "230", "Mauritius"))
    countries.add(Country("mv", "960", "Maldives"))
    countries.add(Country("mw", "265", "Malawi"))
    countries.add(Country("mx", "52", "Mexico"))
    countries.add(Country("my", "60", "Malaysia"))
    countries.add(Country("mz", "258", "Mozambique"))
    countries.add(Country("na", "264", "Namibia"))
    countries.add(Country("nc", "687", "New Caledonia"))
    countries.add(Country("ne", "227", "Niger"))
    countries.add(Country("nf", "672", "Norfolk Islands"))
    countries.add(Country("ng", "234", "Nigeria"))
    countries.add(Country("ni", "505", "Nicaragua"))
    countries.add(Country("nl", "31", "Netherlands"))
    countries.add(Country("no", "47", "Norway"))
    countries.add(Country("np", "977", "Nepal"))
    countries.add(Country("nr", "674", "Nauru"))
    countries.add(Country("nu", "683", "Niue"))
    countries.add(Country("nz", "64", "New Zealand"))
    countries.add(Country("om", "968", "Oman"))
    countries.add(Country("pa", "507", "Panama"))
    countries.add(Country("pe", "51", "Peru"))
    countries.add(Country("pf", "689", "French Polynesia"))
    countries.add(Country("pg", "675", "Papua New Guinea"))
    countries.add(Country("ph", "63", "Philippines"))
    countries.add(Country("pk", "92", "Pakistan"))
    countries.add(Country("pl", "48", "Poland"))
    countries.add(Country("pm", "508", "Saint Pierre And Miquelon"))
    countries.add(Country("pn", "870", "Pitcairn Islands"))
    countries.add(Country("pr", "1", "Puerto Rico"))
    countries.add(Country("ps", "970", "Palestine"))
    countries.add(Country("pt", "351", "Portugal"))
    countries.add(Country("pw", "680", "Palau"))
    countries.add(Country("py", "595", "Paraguay"))
    countries.add(Country("qa", "974", "Qatar"))
    countries.add(Country("re", "262", "Réunion"))
    countries.add(Country("ro", "40", "Romania"))
    countries.add(Country("rs", "381", "Serbia"))
    countries.add(Country("ru", "7", "Russian Federation"))
    countries.add(Country("rw", "250", "Rwanda"))
    countries.add(Country("sa", "966", "Saudi Arabia"))
    countries.add(Country("sb", "677", "Solomon Islands"))
    countries.add(Country("sc", "248", "Seychelles"))
    countries.add(Country("sd", "249", "Sudan"))
    countries.add(Country("se", "46", "Sweden"))
    countries.add(Country("sg", "65", "Singapore"))
    countries.add(Country("sh", "290", "Saint Helena, Ascension And Tristan Da Cunha"))
    countries.add(Country("si", "386", "Slovenia"))
    countries.add(Country("sk", "421", "Slovakia"))
    countries.add(Country("sl", "232", "Sierra Leone"))
    countries.add(Country("sm", "378", "San Marino"))
    countries.add(Country("sn", "221", "Senegal"))
    countries.add(Country("so", "252", "Somalia"))
    countries.add(Country("sr", "597", "Suriname"))
    countries.add(Country("ss", "211", "South Sudan"))
    countries.add(Country("st", "239", "Sao Tome And Principe"))
    countries.add(Country("sv", "503", "El Salvador"))
    countries.add(Country("sx", "1", "Sint Maarten"))
    countries.add(Country("sy", "963", "Syrian Arab Republic"))
    countries.add(Country("sz", "268", "Swaziland"))
    countries.add(Country("tc", "1", "Turks and Caicos Islands"))
    countries.add(Country("td", "235", "Chad"))
    countries.add(Country("tg", "228", "Togo"))
    countries.add(Country("th", "66", "Thailand"))
    countries.add(Country("tj", "992", "Tajikistan"))
    countries.add(Country("tk", "690", "Tokelau"))
    countries.add(Country("tl", "670", "Timor-leste"))
    countries.add(Country("tm", "993", "Turkmenistan"))
    countries.add(Country("tn", "216", "Tunisia"))
    countries.add(Country("to", "676", "Tonga"))
    countries.add(Country("tr", "90", "Turkey"))
    countries.add(Country("tt", "1", "Trinidad &amp; Tobago"))
    countries.add(Country("tv", "688", "Tuvalu"))
    countries.add(Country("tw", "886", "Taiwan"))
    countries.add(Country("tz", "255", "Tanzania, United Republic Of"))
    countries.add(Country("ua", "380", "Ukraine"))
    countries.add(Country("ug", "256", "Uganda"))
    countries.add(Country("us", "1", "United States"))
    countries.add(Country("uy", "598", "Uruguay"))
    countries.add(Country("uz", "998", "Uzbekistan"))
    countries.add(Country("va", "379", "Holy See (vatican City State))"))
    countries.add(Country("vc", "1", "Saint Vincent &amp; The Grenadines"))
    countries.add(Country("ve", "58", "Venezuela, Bolivarian Republic Of"))
    countries.add(Country("vg", "1", "British Virgin Islands"))
    countries.add(Country("vi", "1", "US Virgin Islands"))
    countries.add(Country("vn", "84", "Vietnam"))
    countries.add(Country("vu", "678", "Vanuatu"))
    countries.add(Country("wf", "681", "Wallis And Futuna"))
    countries.add(Country("ws", "685", "Samoa"))
    countries.add(Country("xk", "383", "Kosovo"))
    countries.add(Country("ye", "967", "Yemen"))
    countries.add(Country("yt", "262", "Mayotte"))
    countries.add(Country("za", "27", "South Africa"))
    countries.add(Country("zm", "260", "Zambia"))
    countries.add(Country("zw", "263", "Zimbabwe"))
    return countries
}