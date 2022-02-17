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

case class CustomsOffice(code: String, name: String)

object CustomsOffice {

  implicit val format: OFormat[CustomsOffice] =
    Json.format[CustomsOffice]

  val allCustomsOffices: Seq[CustomsOffice] =
    Seq(
      CustomsOffice("GB000001", "Central Community Transit Office"),
      CustomsOffice("GB000002", "Peter Bennett House"),
      CustomsOffice("GB000003", "Castle Meadow"),
      CustomsOffice("GB000011", "Birmingham Airport"),
      CustomsOffice("GB000014", "Belfast International Airport"),
      CustomsOffice("GB000029", "UKBF, Bristol International Airport"),
      CustomsOffice("GB000033", "Avonmouth 1"),
      CustomsOffice("GB000040", "Dover (OTS) Freight Clearance"),
      CustomsOffice("GB000041", "Folkestone"),
      CustomsOffice("GB000045", "Sheerness"),
      CustomsOffice("GB000047", "Ramsgate"),
      CustomsOffice("GB000049", "Newport"),
      CustomsOffice("GB000051", "Felixstowe"),
      CustomsOffice("GB000052", "Border Force, Harwich"),
      CustomsOffice("GB000053", "Ipswich port"),
      CustomsOffice("GB000054", "Great Yarmouth"),
      CustomsOffice("GB000055", "King's Lynn"),
      CustomsOffice("GB000057", "Rosyth"),
      CustomsOffice("GB000058", "Norwich Airport"),
      CustomsOffice("GB000059", "Grangemouth"),
      CustomsOffice("GB000060", "Dover/ Folkestone Eurotunnel Freight"),
      CustomsOffice("GB000062", "Scrabster"),
      CustomsOffice("GB000063", "Scotland Inland Authorised Consignee/ Consignor"),
      CustomsOffice("GB000067", "Scotland Frontier"),
      CustomsOffice("GB000070", "Prestwick Airport"),
      CustomsOffice("GB000072", "Hull"),
      CustomsOffice("GB000073", "Grimsby"),
      CustomsOffice("GB000074", "Immingham"),
      CustomsOffice("GB000080", "Liverpool, Seaforth S2 berth"),
      CustomsOffice("GB000081", "UK North Authorised Consignor/nees"),
      CustomsOffice("GB000084", "London Heathrow Cargo"),
      CustomsOffice("GB000085", "London Gatwick Airport Cargo Dist."),
      CustomsOffice("GB000087", "Leeds Bradford Airport"),
      CustomsOffice("GB000091", "Southend Airport"),
      CustomsOffice("GB000093", "Tilbury"),
      CustomsOffice("GB000098", "Newcastle Airport"),
      CustomsOffice("GB000099", "Thames Europort (DIFT)"),
      CustomsOffice("GB000102", "Luton Airport FCT"),
      CustomsOffice("GB000108", "Falmouth"),
      CustomsOffice("GB000119", "Northolt Airport"),
      CustomsOffice("GB000120", "London Heathrow Airport (Baggage)"),
      CustomsOffice("GB000121", "Stansted Airport FCT"),
      CustomsOffice("GB000122", "Cardiff International Airport"),
      CustomsOffice("GB000123", "Swansea"),
      CustomsOffice("GB000124", "Holyhead"),
      CustomsOffice("GB000125", "Barry Dock"),
      CustomsOffice("GB000126", "Pembroke Dock"),
      CustomsOffice("GB000128", "Edinburgh Airport"),
      CustomsOffice("GB000130", "London City Airport"),
      CustomsOffice("GB000140", "George Best (Belfast City)"),
      CustomsOffice("GB000142", "Belfast Docks, Unit 2, Block C"),
      CustomsOffice("GB000149", "Purfleet Thames Terminal"),
      CustomsOffice("GB000150", "Thamesport"),
      CustomsOffice("GB000155", "Reading"),
      CustomsOffice("GB000170", "London Gateway Port"),
      CustomsOffice("GB000191", "Manchester"),
      CustomsOffice("GB000218", "Newcastle-upon-Tyne"),
      CustomsOffice("GB000219", "Teesport"),
      CustomsOffice("GB000244", "Boston"),
      CustomsOffice("GB000245", "UKBA East Midlands Airport"),
      CustomsOffice("GB000246", "UK South Authorised Consignor/nees"),
      CustomsOffice("GB000250", "Plymouth"),
      CustomsOffice("GB000290", "Southampton"),
      CustomsOffice("GB000291", "Poole Ferry Terminal"),
      CustomsOffice("GB000292", "Portsmouth"),
      CustomsOffice("GB000297", "Exeter Airport"),
      CustomsOffice("GB000328", "Cardiff"),
      CustomsOffice("GB000383", "Salford National Clearance Hub"),
      CustomsOffice("GB000411", "Aberdeen Airport"),
      CustomsOffice("GB000416", "Peterhead"),
      CustomsOffice("GB000434", "Aberdeen"),
      CustomsOffice("GB000450", "Isle of Man Customs & Excise"),
      CustomsOffice("GB000461", "Guernsey Customs & Excise"),
      CustomsOffice("GB000465", "Jersey Customs & Immigration"),
      CustomsOffice("GB000480", "Chatham"),
      CustomsOffice("GB000500", "Coventry International Hub"),
      CustomsOffice("GB000501", "HWDC Langley"),
      CustomsOffice("GB000921", "Fords Jetty"),
      CustomsOffice("GB001016", "Lerwick"),
      CustomsOffice("GB003010", "Barrow in Furness"),
      CustomsOffice("GB003020", "Berwick upon Tweed"),
      CustomsOffice("GB003030", "Blackpool International Airport"),
      CustomsOffice("GB003040", "Glasgow Docks"),
      CustomsOffice("GB003050", "Plymouth Docks"),
      CustomsOffice("GB003060", "Bournemouth (Hurn) Airport"),
      CustomsOffice("GB003070", "Brize Norton"),
      CustomsOffice("GB003080", "Cambridge Airport"),
      CustomsOffice("GB003090", "Southampton (Eastleigh) Airport"),
      CustomsOffice("GB003100", "Dundee (Sea)"),
      CustomsOffice("GB003110", "Farnborough Airport"),
      CustomsOffice("GB003120", "Gloucester (Staverton) Airport"),
      CustomsOffice("GB003130", "Humberside International Airport"),
      CustomsOffice("GB003140", "Hartlepool"),
      CustomsOffice("GB003150", "Inverness"),
      CustomsOffice("GB003160", "Kemble Airport"),
      CustomsOffice("GB003170", "Kirkwall (Orkney Islands)"),
      CustomsOffice("GB003180", "Durham Tees Valley (Teeside Apt)"),
      CustomsOffice("GB003190", "Liverpool John Lennon Airport"),
      CustomsOffice("GB003200", "London Ashford (Lydd) Airport"),
      CustomsOffice("GB003210", "Londonderry"),
      CustomsOffice("GB003220", "Manchester Docks"),
      CustomsOffice("GB003230", "Oxford (Kidlington) Airport"),
      CustomsOffice("GB003240", "Portree (Isle of Skye)"),
      CustomsOffice("GB003250", "Newhaven"),
      CustomsOffice("GB003260", "Stornoway (Isle of Lewis)"),
      CustomsOffice("GB003270", "Stornoway Airport"),
      CustomsOffice("GB003280", "Workington"),
      CustomsOffice("GB003290", "Biggin Hill"),
      CustomsOffice("GB004098", "Glasgow"),
      CustomsOffice("GB005010", "Appledore, c/o National Simps Team"),
      CustomsOffice("GB005020", "Bridgwater, c/o National Simps Team"),
      CustomsOffice("GB005030", "Dagenham, c/o National Simps Team"),
      CustomsOffice("GB005040", "Dartford, c/o National Simps Team"),
      CustomsOffice("GB005050", "Greenock, c/o Simps Team"),
      CustomsOffice("GB005060", "Langstone, c/o Simps Team"),
      CustomsOffice("GB005070", "London, c/o Simps Team"),
      CustomsOffice("GB005080", "Middlesbrough, c/o Simps Team"),
      CustomsOffice("GB005090", "Neath, c/o Simps Team"),
      CustomsOffice("GB005100", "Penrhyn, c/o Simps Team"),
      CustomsOffice("GB005110", "Ridham, c/o Simps Team"),
      CustomsOffice("GB005120", "Rochester, c/o Simps Team"),
      CustomsOffice("GB005130", "Shoreham, c/o Simps Team"),
      CustomsOffice("GB005140", "South Shields, c/o Simps Team"),
      CustomsOffice("GB005160", "Warrenpoint"),
      CustomsOffice("GB005170", "Killingholme"),
      CustomsOffice("GB005180", "Montrose"),
      CustomsOffice("GB005190", "Portbury"),
      CustomsOffice("GB005200", "Fishguard"),
      CustomsOffice("GB005210", "Heysham"),
    )

  def selectItems(kindsOfPackage: Seq[CustomsOffice]): Seq[SelectItem] =
    SelectItem(value = None, text = "Select a customs office") +:
      kindsOfPackage.map {
        kindOfPackage =>
          SelectItemViewModel(
            value = kindOfPackage.code,
            text  = kindOfPackage.name
          )
      }
}