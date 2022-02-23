/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models

import play.api.libs.json.{Json, OFormat}
import uk.gov.hmrc.govukfrontend.views.viewmodels.select.SelectItem
import viewmodels.govuk.select._

final case class DocumentType(code: String, name: String)

object DocumentType {

  implicit val format: OFormat[DocumentType] =
    Json.format[DocumentType]

  val allDocumentTypes: Seq[DocumentType] =
    Seq(
      DocumentType("A001", """Cert. of auth. - EMPEROR grapes"""),
      DocumentType("A004", """Cert. of auth. - Tobacco"""),
      DocumentType("A006", """Cert. of auth. - Beef and Veal"""),
      DocumentType("A008", """Cert. of auth. - oranges h/quality"""),
      DocumentType("A009", """Cert. of auth. - fresh minneola"""),
      DocumentType("A010", """Cert. of auth. - conc orange juice"""),
      DocumentType("A014", """Cert. of auth. - HANDI"""),
      DocumentType("A015", """Cert. of auth. - silk/cotton"""),
      DocumentType("A017", """Cert. of auth. - Reg.936/97"""),
      DocumentType("A018", """Certificate for Cheese Fondues"""),
      DocumentType("A019", """Certificate of Nitrate quality"""),
      DocumentType("A021", """Cert. of auth. - Reg.264/04"""),
      DocumentType("A022", """Cert. of auth. - "Basmati Rice""""),
      DocumentType("A023", """Certificate of authenticity as ment"""),
      DocumentType(
        "A119",
        """Airworthiness certificate or declaration in the commercial invoice cont. the elements of the airworthiness certif. issued or a doc. Annexed"""
      ),
      DocumentType("C001", """Attestation of equivalence"""),
      DocumentType("C003", """Bluefin tuna statistical document"""),
      DocumentType("C004", """Surveillance document"""),
      DocumentType("C012", """Cert. for export of pasta to USA"""),
      DocumentType("C013", """Certificate IMA 1"""),
      DocumentType("C014", """V I 1 document"""),
      DocumentType("C015", """V I 2 extract"""),
      DocumentType("C017", """V I 1 document Reg.883/01"""),
      DocumentType("C018", """V I 2 extract Reg.883/01"""),
      DocumentType("C019", """Outward processing authorization"""),
      DocumentType("C024", """Import licence Reg.2640/98"""),
      DocumentType("C026", """Cert. of herdbook registration"""),
      DocumentType("C027", """Pedigree certificate"""),
      DocumentType(
        "C028",
        """Invoice declaration bearing :"Specific origin rule laid down in Dec. No 2/2000 of EC-Mexico Joint Council Annex III App. IIa Note 12.1""""
      ),
      DocumentType(
        "C031",
        """Statement with the following form "ILO Conventions No 29, 87, 98, 100, 105, 111, 138 and 182 - Titel III of Regulation (EC) No 2501/01""""
      ),
      DocumentType("C032", """Export certificate Decision 822/01"""),
      DocumentType("C034", """"Kimberley" Community certificate"""),
      DocumentType("C036", """Supplementary document Reg.1159/03"""),
      DocumentType("C038", """ICCAT bluefin tuna document"""),
      DocumentType("C039", """ICCAT swordfish document"""),
      DocumentType("C040", """ICCAT/IOTC bigeye tuna document"""),
      DocumentType("C041", """ICCAT bluefin tuna re-export"""),
      DocumentType("C042", """ICCAT swordfish re-export"""),
      DocumentType("C043", """ICCAT/IOTC bigeye tuna re-export"""),
      DocumentType("C044", """Certificate Mineral waters - NO"""),
      DocumentType("C046", """Banana weighing certificate"""),
      DocumentType(
        "C049",
        """Supplementary document as mentioned in Reg. (EC) No 1100/2006 (OJ L 196)"""
      ),
      DocumentType("C050", """Certificate Reg.1234/2007"""),
      DocumentType(
        "C051",
        """Declaration by the Food-veterinary and environmental agency of Faeroe islands, issued in accordance with Regulation (EC) No 1381/2007"""
      ),
      DocumentType("C052", """Export aut. restricted goods/techno"""),
      DocumentType("C400", """Presentation of CITES certificate"""),
      DocumentType("C500", """Request of duty relief Reg.2658/87"""),
      DocumentType("C600", """Authorisation to operate warehouse"""),
      DocumentType("C601", """Inward processing Authorisation"""),
      DocumentType("C602", """Declaration of value D.V.1BIS"""),
      DocumentType("C603", """Information sheet INF1"""),
      DocumentType("C604", """Information sheet INF2"""),
      DocumentType("C605", """Information sheet INF3"""),
      DocumentType("C606", """Information sheet INF5"""),
      DocumentType("C607", """Information sheet INF6"""),
      DocumentType("C608", """Information sheet INF7"""),
      DocumentType("C609", """Information sheet INF8"""),
      DocumentType("C610", """Information sheet INF9"""),
      DocumentType("C611", """Information document"""),
      DocumentType("C612", """Transit declaration T2F"""),
      DocumentType("C613", """Consignment note CIM (T2)"""),
      DocumentType("C614", """Consignment note CIM (T2F)"""),
      DocumentType("C615", """TR transfer note (T1)"""),
      DocumentType("C616", """TR transfer note (T2)"""),
      DocumentType("C617", """TR transfer note (T2F)"""),
      DocumentType("C618", """Air manifest (T2F)"""),
      DocumentType("C619", """Maritime manifest (T2F)"""),
      DocumentType("C620", """T2LF document"""),
      DocumentType("C621", """T2M document"""),
      DocumentType("C622", """Certificate of customs status"""),
      DocumentType("C623", """Transhipment certificate EXP.1"""),
      DocumentType("C624", """Form 302"""),
      DocumentType("C625", """Rhine Manifest"""),
      DocumentType("C626", """Binding tariff information"""),
      DocumentType("C627", """Binding origin information"""),
      DocumentType("C628", """Attestation of equivalence/control"""),
      DocumentType("C629", """Pedigree/zoo technical certificate"""),
      DocumentType(
        "C633",
        """Certif. of indust. use for  fruit and vegetab. subject to comm. marketing stand. in acc. with Art. 19(2) and Annex V of Reg. (EC) 1580/2007"""
      ),
      DocumentType("C634", """Proof of origin"""),
      DocumentType("C635", """Label"""),
      DocumentType("C638", """Import permit"""),
      DocumentType("C639", """Import notification"""),
      DocumentType(
        "C640",
        """Common veterinary entry document (CVED) in accordance with Regulation (EC) No. 282/2004, used for veterinary checks on live animals"""
      ),
      DocumentType("C641", """Dissostichus â€“ catch documet import"""),
      DocumentType("C644", """Certificate of inspection"""),
      DocumentType("C645", """Certificate for military equipment"""),
      DocumentType("C647", """Confirmation of receipt"""),
      DocumentType("C648", """Boned meat certificate"""),
      DocumentType("C649", """Refund certificate"""),
      DocumentType("C650", """Consignment note"""),
      DocumentType("C651", """Accompanying admin. document"""),
      DocumentType("C652", """Accompanying docs for wine"""),
      DocumentType("C653", """Confirmation (export to Iraq)"""),
      DocumentType("C654", """Auth. for medical products"""),
      DocumentType("C655", """Certificate - product proof"""),
      DocumentType("C656", """Dissostichus â€“ Catch document expor"""),
      DocumentType("C657", """Sanitary certificate"""),
      DocumentType("C659", """Prior written declaration"""),
      DocumentType("C660", """Export notification"""),
      DocumentType("C661", """Explicit consent"""),
      DocumentType("C662", """Import decision"""),
      DocumentType("C663", """Export refund rate Art8a EC800/1999"""),
      DocumentType("C664", """CN22 declarat. Art 237 Reg 2454/93"""),
      DocumentType("C665", """CN23 declarat. Art 237 Reg 2454/93"""),
      DocumentType("C666", """Certificate issued by the FGIS"""),
      DocumentType("C667", """Laboratory analysis"""),
      DocumentType("C668", """Cert. by USA wet milling indtry"""),
      DocumentType("C669", """Notification document Reg 1013/2006"""),
      DocumentType("C670", """Movement document Reg 1013/2006"""),
      DocumentType("C673", """Catch certificate"""),
      DocumentType("C674", """Health Certificate for the importat"""),
      DocumentType("C675", """Analytical report"""),
      DocumentType("C678", """Common entry document (CED)"""),
      DocumentType("D003", """Original Production certificate"""),
      DocumentType("D005", """Commercial invoice undertakings"""),
      DocumentType("D006", """Certificate of CCCME Reg. 1531/02"""),
      DocumentType("D007", """Chemical analysis certificate"""),
      DocumentType("D008", """Invoice with a signed declaration"""),
      DocumentType(
        "D009",
        """Import auth. issued in acc. with Reg. 1818/2006 in rel. with anti-dumping measures on imp. of potassium chloride origin. in Belarus"""
      ),
      DocumentType(
        "D010",
        """(Non-customised) multi-comb. forms of DRAMs orig. in count. other than the Rep. of Korea or orig. in the Rep. of Korea and manuf. by Samsung"""
      ),
      DocumentType(
        "D011",
        """DRAM containing products of KR origin other than from Samsung <|10% of the value"""
      ),
      DocumentType(
        "D012",
        """DRAM containing products of KR origin other than from Samsung >=|10% <|20% of the value"""
      ),
      DocumentType(
        "D013",
        """DRAM containing products of KR origin other than from Samsung >=|20% <|30% of the value"""
      ),
      DocumentType(
        "D014",
        """DRAM containing products of KR origin other than from Samsung >=|30% <|40% of the value"""
      ),
      DocumentType(
        "D015",
        """DRAM containing products of KR origin other than from Samsung >=|40% <|50% of the value"""
      ),
      DocumentType(
        "D016",
        """DRAM containing products of KR origin other than from Samsung >=|50% of the value"""
      ),
      DocumentType("E012", """Export licence "Cultural goods""""),
      DocumentType("E013", """Export licence "controlled substanc"""),
      DocumentType("E014", """Export certificate - Milk products"""),
      DocumentType("E015", """N/A"""),
      DocumentType(
        "E016",
        """Export document issued by the competent Czech authorities(Regulation (EC) No ^  925/2003-L 131)"""
      ),
      DocumentType("E017", """Export certificates (3rd countries)"""),
      DocumentType("E990", """Export auth. goods for torture"""),
      DocumentType("I001", """Import auth. by MS valid C.ty"""),
      DocumentType("I003", """Import auth. by MS valid EC"""),
      DocumentType("I004", """Surveillance document"""),
      DocumentType("I005", """Surveillance document (Reg.1499/02)"""),
      DocumentType("K014", """Tariff quotas (09.1558/1559)"""),
      DocumentType("K016", """Tariff quotas (09.1588/1589)"""),
      DocumentType("K018", """Quotas 09.1512/09.1513 exhausted"""),
      DocumentType("K019", """Tariff q. - order 09.1514 exhausted"""),
      DocumentType("K19", """Tariff q. - order 09.1514 exhausted"""),
      DocumentType("L001", """Import licence AGRIM"""),
      DocumentType("L079", """Textile products: import licence"""),
      DocumentType("L081", """Certificate of analysis (Regulation"""),
      DocumentType("L082", """Certificate of conformity (Regulati"""),
      DocumentType("L085", """Transport document Reg.196/97"""),
      DocumentType("L097", """Import licence by MS valid C.ty"""),
      DocumentType("L100", """Import licence (ozone)"""),
      DocumentType("L102", """Import certificate Reg.1296/03"""),
      DocumentType("L106", """Licence of conformity of hemp"""),
      DocumentType(
        "L108",
        """Import lic. with the following entries: the country or countries of origin, the quantity of raw sugar, expressed as white sugar equivalent"""
      ),
      DocumentType("L109", """Import licence (rice)"""),
      DocumentType("L113", """Import authorization (602/02)"""),
      DocumentType("L114", """Import authorization (1469/02)"""),
      DocumentType("L116", """"Kimberley" certificate"""),
      DocumentType("L119", """Import authorization (893/03)"""),
      DocumentType("L130", """Accesion licence (Bananas) trad"""),
      DocumentType("L131", """Accesion licence (Bananas) non-trad"""),
      DocumentType("L132", """Import authorisation"""),
      DocumentType("L134", """Import aut. restricted goods/techno"""),
      DocumentType("L135", """Import authorisation (precursors)"""),
      DocumentType("L136", """Goods, containing, or relying on, o"""),
      DocumentType(
        "N002",
        """Certif. of conf. with the comm. marketing stand. for fruit and vegetables, in acc. with Art. 11,12 and Annex III of Reg. (EC) 1580/2007"""
      ),
      DocumentType("N003", """Certificate of quality"""),
      DocumentType("N018", """ATR certificate"""),
      DocumentType("N235", """Container list"""),
      DocumentType("N271", """Packing list"""),
      DocumentType("N325", """Proforma invoice"""),
      DocumentType("N380", """Commercial invoice"""),
      DocumentType("N703", """House waybill"""),
      DocumentType("N704", """Master bill of lading"""),
      DocumentType("N705", """Bill of lading"""),
      DocumentType("N710", """Maritime manifest (T1)"""),
      DocumentType("N714", """House bill of lading"""),
      DocumentType("N720", """Consignment note CIM"""),
      DocumentType("N722", """Road list - SMGS"""),
      DocumentType("N730", """Road consignment note"""),
      DocumentType("N740", """Air manifest (T1)"""),
      DocumentType("N741", """Master airwaybill"""),
      DocumentType("N750", """Movement by post"""),
      DocumentType("N760", """Multimodal/combined transport doc."""),
      DocumentType("N785", """Cargo manifest"""),
      DocumentType("N787", """Bordereau (cargo load list)"""),
      DocumentType("N820", """Transit declaration "T""""),
      DocumentType("N821", """Transit declaration T1"""),
      DocumentType("N822", """Transit declaration T2"""),
      DocumentType("N823", """T5 control copy"""),
      DocumentType("N825", """T2L document"""),
      DocumentType("N830", """Goods declaration for exportation"""),
      DocumentType("N851", """Phytosanitary certificate"""),
      DocumentType("N852", """Analysis and health certificate"""),
      DocumentType(
        "N853",
        """Common Veterinary Entry Document (CVED) in accordance with Regulation (EC) No. 136/2004, used for veterinary check on products."""
      ),
      DocumentType("N861", """Universal certificate of origin"""),
      DocumentType("N862", """Declaration of origin"""),
      DocumentType("N864", """Preference certificate of origin"""),
      DocumentType("N865", """Certificate of origin Form A"""),
      DocumentType("N933", """Cargo declaration (arrival)"""),
      DocumentType("N934", """Declaration of customs value D.V.1"""),
      DocumentType("N935", """Invoice on which value is declared"""),
      DocumentType("N941", """Embargo permit"""),
      DocumentType("N951", """TIF form"""),
      DocumentType("N952", """TIR Carnet"""),
      DocumentType("N954", """Movement certificate EUR.1"""),
      DocumentType("N955", """ATA carnet"""),
      DocumentType("N990", """Customs procedure authorisation"""),
      DocumentType("P001", """Maize and maize products"""),
      DocumentType("P002", """Other cereal products"""),
      DocumentType("P300", """Cereals (add. codes P301-P323)"""),
      DocumentType("P400", """Rice (add. codes P401-P409)"""),
      DocumentType("P500", """Eggs (add. codes P501-P507)"""),
      DocumentType("P600", """Sugar, molasses, isoglucose"""),
      DocumentType("P700", """Milk, milk products (P701-707)"""),
      DocumentType("R001", """Refund cert. rate not fixed in adv."""),
      DocumentType("R002", """Refund cert. rate fixed in adv."""),
      DocumentType("R003", """not covered by refund certificate"""),
      DocumentType(
        "U002",
        """Certificate of origin for imports of agricultural products into the European Community."""
      ),
      DocumentType("U003", """Cert.of origin Art.47 Reg.2453/93"""),
      DocumentType("U004", """Cert.of origin Art.55 Reg.2453/93"""),
      DocumentType("U005", """Certificate of origin designation"""),
      DocumentType("U011", """Import licence Reg. 896/01"""),
      DocumentType("U014", """Cert. of origin Form A(Reg.1613/00)"""),
      DocumentType("U015", """Cert. of origin Form A(Reg.1614/00)"""),
      DocumentType("U016", """Cert. of origin Form A(Reg.1615/00)"""),
      DocumentType(
        "U019",
        """Certificate EUR.1 bearing:"Specific origin rule laid down in Dec. No 2/2000 of EC-Mexico Joint Council Annex III Appendix IIa Note 12.1""""
      ),
      DocumentType("U021", """Certificate EUR.1 (Dec.1/01)"""),
      DocumentType("U022", """Certificate EUR.1 (Dec.2/01)"""),
      DocumentType("U023", """Certificate EUR.1 (Dec.1/03)"""),
      DocumentType("U024", """Certificate EUR.1 (Dec.3/01)"""),
      DocumentType("U026", """Certificate EUR.1 (Dec.657/01)"""),
      DocumentType(
        "U027",
        """Certif. of orig. Form A bear. "Quota order No 09.4305-Reg(EC) No 1381/2002" the date of load. of sugar, the marketing year, CN code 17011110"""
      ),
      DocumentType(
        "U028",
        """Certif. of orig. Form A bear.."Regulation (EC) No 964/2007"; the date of loading of the rice, the marketing year, CN code 1006"""
      ),
      DocumentType(
        "U030",
        """Statement with the following  "ILO Conventions No 29, 87 ,98, 100, 105, 111, 138 and 182 - Title III of Reg.(EC) No 2501/01""""
      ),
      DocumentType("U031", """Certificate EUR 1. bearing the endo"""),
      DocumentType("U033", """Certificate EUR.1 (Dec.2002/644)"""),
      DocumentType("U034", """Certificate EUR.1 (Dec.2/02)"""),
      DocumentType("U035", """Certificate EUR.1 (Dec.2003/673)"""),
      DocumentType(
        "U038",
        """The proof of origin shall contain the following mention: "sheep product/s from the species domestic sheep""""
      ),
      DocumentType(
        "U039",
        """The proof of origin shall contain the following mention: "from the species other than domestic sheep""""
      ),
      DocumentType(
        "U040",
        """Proof of orig. est. in acc. with Art. 47 of Reg.(EEC) No 2454/93, inc. CN code, the order num.of tariff quota, the total net weight"""
      ),
      DocumentType("U041", """Certificate EUR.1 1/2005 (CV)"""),
      DocumentType("U042", """Certificate EUR.1 2/2005"""),
      DocumentType(
        "U043",
        """Certificate EUR.1 bearing the endorsement "Derogation-Commission Decision 2005/578/EC" or "Dérogation-Décision 2005/578/CE de la Commission""""
      ),
      DocumentType("U044", """Certificate EUR.1 1/2005 (TN)"""),
      DocumentType("U045", """Movement certificate EUR-MED"""),
      DocumentType("U046", """Certificate EUR.1 3/2005"""),
      DocumentType("U047", """Certificate EUR.1 1/2005 (MA)"""),
      DocumentType("U048", """Invoice declaration EUR-MED"""),
      DocumentType("U049", """Certificate EUR.1 " 2007/167/EC""""),
      DocumentType("U050", """Certificate EUR.1 " 2007/767/EC""""),
      DocumentType("U051", """Certificate EUR.1 "2008/3568/EC""""),
      DocumentType("U052", """Derogation Reg (EC) No 815/2008"""),
      DocumentType("U053", """Derogation Decision 2008/691/EC"""),
      DocumentType("U054", """Certificate EUR.1  "2008/751/EC""""),
      DocumentType("U055", """Cert. EUR.1 DEROG.-DEC. 2008/820/EC"""),
      DocumentType("U056", """Cert EUR.1 Derog-Dec 2008/886/EC"""),
      DocumentType(
        "U090",
        """Certificate EUR 1,stating EC origin, in the context of the agreement between the European Economic Community and the Swiss Confederation"""
      ),
      DocumentType(
        "U091",
        """Invoice declaration, stating EC origin in the context of the agreement beetween the European Economic Community and the Swiss Confederation"""
      ),
      DocumentType("X001", """Export licence AGREX"""),
      DocumentType("X002", """Dual use export authorisation"""),
      DocumentType("X010", """Military export Reg 423/2007"""),
      DocumentType("X011", """Military export Reg 329/2007"""),
      DocumentType("X012", """Export author. by MS of exporter"""),
      DocumentType("X035", """Export authorisation (precursors)"""),
      DocumentType("Y001", """Wholly obtained in Lebanon"""),
      DocumentType("Y003", """Wholly obtained in Tunisia"""),
      DocumentType("Y005", """Wholly obtained in Algeria"""),
      DocumentType("Y006", """Stamp"""),
      DocumentType("Y007", """Seal"""),
      DocumentType("Y008", """Transported directly from Turkey"""),
      DocumentType("Y009", """Textile Reimportation Reg.3036/94"""),
      DocumentType(
        "Y010",
        """Products must meet rel. req. of Reg.(EC) 852/2004 and 853/2004, compl. with health marking req. laid in AnnexII, Sec.I of Reg.(EC) 853/2004"""
      ),
      DocumentType(
        "Y011",
        """Prod. must meet req. of Reg(EC) 852/2004 and 853/2004, compl. with the health marking req. laid in Annex I, Sec.I, Ch.III of Reg(EC)854/2004"""
      ),
      DocumentType(
        "Y013",
        """Insertion of following end. in the "Remarks" of goods movement certificate. Reg(EEC) 1518/76 (OJ L 169/37):Special export charge collected"""
      ),
      DocumentType(
        "Y015",
        """The rough diamonds are cont. in tamper-resistant containers and seals applied at export by the participant(Kimberley process) are not broken"""
      ),
      DocumentType("Y016", """Transported directly Reg.1964/03"""),
      DocumentType("Y017", """Wholly obtained in Jordan"""),
      DocumentType("Y018", """Meat of specific breeds"""),
      DocumentType("Y019", """Req. preferential treatment Iceland"""),
      DocumentType("Y020", """Req. preferential treatment Norway"""),
      DocumentType("Y021", """Req. EEA preferential treatment"""),
      DocumentType("Y022", """Consignor / exporter (AEO)"""),
      DocumentType("Y023", """Consignee (AEO certificate number)"""),
      DocumentType("Y024", """Declarant (AEO certificate number)"""),
      DocumentType("Y025", """Representative  (AEO)"""),
      DocumentType("Y026", """Principal (AEO certificate number)"""),
      DocumentType("Y027", """Warehousekeeper (AEO)"""),
      DocumentType("Y028", """Carrier (AEO certificate number)"""),
      DocumentType("Y029", """Other authorised economic operator"""),
      DocumentType("Y030", """Transp direct to EC RegEC 1039/2008"""),
      DocumentType(
        "Y040",
        """The VAT id. no. issued in MS of importation for the importer design. or recognised under Art.201 of  VAT  Dir. as liable for payment of  VAT"""
      ),
      DocumentType(
        "Y041",
        """The VAT id no of the customer  who is liable for the VAT on the intra-Community acquisition of goods in accordance with Art.200 of VAT Dir"""
      ),
      DocumentType("Y042", """The VAT identification number issue"""),
      DocumentType("Y100", """Import licence AGRIM"""),
      DocumentType("Y30", """Transp direct to EC RegEC 1039/2008"""),
      DocumentType("Y900", """Declared goods not in CITES"""),
      DocumentType("Y901", """Product not in the dual use list"""),
      DocumentType("Y902", """Goods not in the OZ footnotes"""),
      DocumentType("Y903", """Goods not in list of cultural goods"""),
      DocumentType("Y904", """Goods not in the TR footnotes"""),
      DocumentType(
        "Y905",
        """Goods that will be used for exclusive purpose of public display in museum in view of their historic significance or medical-technical goods"""
      ),
      DocumentType("Y906", """Goods not in the TR footnotes (708)"""),
      DocumentType(
        "Y907",
        """Goods to be used by military or civil personnel of MS, taking part in EU or UN peace keeping or crisis man. op. in the 3 country of dest"""
      ),
      DocumentType(
        "Y908",
        """Exp.to terr.of MS(Greenl., New Cal. and Dep, FR Polyn, FR Sth and Antarctic Terr, Wallis, Futuna Is, Mayotte, St Pierre, Miquelon, Büsingen)"""
      ),
      DocumentType("Y909", """Not concerned by Reg 1984/2003"""),
      DocumentType("Y911", """Do not correspond to MG footnotes"""),
      DocumentType("Y912", """Not concerned by Reg (EC) 194/2008"""),
      DocumentType("Y913", """Export refund rate, according to Ar"""),
      DocumentType("Y914", """Refund amounts of less than EUR 1 0"""),
      DocumentType("Y915", """Reference Identification Number"""),
      DocumentType("Y916", """Product not in RegEC 689/2008,Anx I"""),
      DocumentType("Y917", """Product not in RegEC 689/2008,Anx V"""),
      DocumentType("Y919", """Chemicals Art2(2)(i) RegEC 689/2008"""),
      DocumentType("Y920", """Goods other than in footnotes"""),
      DocumentType("Y921", """Goods exempted from the prohibition"""),
      DocumentType("Y922", """Other cats/dogs fur Reg 1523/2007"""),
      DocumentType(
        "Y924",
        """Goods other than metallic mercury as mentioned by Regulation (EC) No 1102/2008"""
      ),
      DocumentType("Y925", """Export for research and development, medical or analysis purposes"""),
      DocumentType("Y926", """Goods not concerned by import prohibition"""),
      DocumentType("Y927", """The declared goods are not concerned""")
    )

  def selectItems(documentType: Seq[DocumentType]): Seq[SelectItem] =
    SelectItem(value = None, text = "Select a document type") +:
      documentType.map { doc =>
        SelectItemViewModel(
          value = doc.code,
          text = s"${doc.code}: ${doc.name}"
        )
      }
}
