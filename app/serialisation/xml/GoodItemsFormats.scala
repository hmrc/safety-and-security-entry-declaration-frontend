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

package serialisation.xml

import models.{Container, Country, Document, DocumentType, PaymentMethod}
import models.completion.downstream._
import scala.xml.NodeSeq
import serialisation.xml.XmlImplicits._

trait GoodItemsFormats
  extends CommonFormats
  with TransportFormats
  with PackagesFormats
  with PartyFormats {

  implicit val containerFmt = new Format[Container] {
    override def encode(container: Container): NodeSeq = {
      <ConNumNR21>{container.itemContainerNumber}</ConNumNR21>
    }
    override def decode(data: NodeSeq): Container = Container(data.text)
  }

  implicit val documentTypeFormat = new StringFormat[DocumentType] {
    override def encode(docType: DocumentType): String = docType.code
    override def decode(s: String): DocumentType = {
      DocumentType.allDocumentTypes.find { _.code == s }.getOrElse {
        throw new XmlDecodingException(s"Bad Document code: $s")
      }
    }
  }

  implicit val documentFormat = new Format[Document] {
    override def encode(doc: Document): NodeSeq = {
      <DocTypDC21>{doc.documentType.toXmlString}</DocTypDC21>
      <DocRefDC23>{doc.reference}</DocRefDC23>
    }
    override def decode(data: NodeSeq): Document = {
      Document(
        documentType = (data \\ "DocTypDC21").text.parseXmlString[DocumentType],
        reference = (data \\ "DocRefDC23").text
      )
    }
  }

  implicit val goodsItemIdentityByCodeFmt = new Format[GoodsItemIdentity.ByCommodityCode] {
    override def encode(id: GoodsItemIdentity.ByCommodityCode): NodeSeq = {
      <COMCODGODITM>{id.code}</COMCODGODITM>
    }
    override def decode(data: NodeSeq): GoodsItemIdentity.ByCommodityCode = {
      GoodsItemIdentity.ByCommodityCode((data \\ "COMCODGODITM").text)
    }
  }

  implicit val goodsItemIdentityWithDescFmt = new Format[GoodsItemIdentity.WithDescription] {
    override def encode(id: GoodsItemIdentity.WithDescription): NodeSeq = {
      <GooDesGDS23>{id.desc}</GooDesGDS23>
    }
    override def decode(data: NodeSeq): GoodsItemIdentity.WithDescription = {
      GoodsItemIdentity.WithDescription((data \\ "GooDesGDS23").text)
    }
  }

  implicit val goodsItemIdentityFmt = new Format[GoodsItemIdentity] {
    override def encode(id: GoodsItemIdentity): NodeSeq = id match {
      case g: GoodsItemIdentity.ByCommodityCode => g.toXml
      case g: GoodsItemIdentity.WithDescription => g.toXml
    }

    override def decode(data: NodeSeq): GoodsItemIdentity = {
      if ((data \\ "COMCODGODITM").nonEmpty) {
        data.parseXml[GoodsItemIdentity.ByCommodityCode]
      } else {
        data.parseXml[GoodsItemIdentity.WithDescription]
      }
    }
  }

  implicit val dangerousGoodCodeFmt: StringFormat[DangerousGoodsCode] = {
    StringFormat.simple(_.code, c => DangerousGoodsCode(c))
  }

  implicit val loadingPlaceFmt = new StringFormat[LoadingPlace] {
    override def encode(id: LoadingPlace): String = s"${id.country.toXmlString} ${id.desc}"

    override def decode(s: String): LoadingPlace = s.split(" ").toList match {
      case countryCode :: remainder =>
        LoadingPlace(
          countryCode.parseXmlString[Country],
          remainder.mkString(" ")
        )
      case _ =>
        throw new XmlDecodingException(s"Unexpected format for loading place: '$s'")
    }
  }

  implicit val specialMentionFmt: StringFormat[SpecialMention] = {
    StringFormat.simple(_.code, SpecialMention(_))
  }

  implicit val goodsItemFmt = new Format[GoodsItem] {
    override def encode(item: GoodsItem): NodeSeq = {
      val grossMass: NodeSeq = item.grossMass.map { m =>
        <GroMasGDS46>{m.toXmlString}</GroMasGDS46>
      }.toSeq

      val dangerousGoodsCode: NodeSeq = item.dangerousGoodsCode.map { c =>
        <UNDanGooCodGDI1>{c.toXmlString}</UNDanGooCodGDI1>
      }.toSeq

      val documents: NodeSeq = item.documents map { d => <PRODOCDC2>{d.toXml}</PRODOCDC2> }

      val specialMentions: NodeSeq = item.specialMentions map {
        sm => <SPEMENMT2>{sm.toXmlString}</SPEMENMT2>
      }

      val consignee: NodeSeq = item.consignee.map { c =>
        <TRACONCE2>{goodsConsigneeFormat.encode(c)}</TRACONCE2>
      }.toSeq

      val containers: NodeSeq = item.containers map { c => <CONNR2>{c.toXml}</CONNR2> }

      val packages: NodeSeq = item.packages map { p => <PACGS2>{p.toXml}</PACGS2> }

      val notifiedParty: NodeSeq = item.notifiedParty.map { np =>
        <PRTNOT640>{goodsNotifiedPartyFormat.encode(np)}</PRTNOT640>
      }.toSeq

      // The ItemIdentity type spans two fields which need to be in different positions in the seq
      // of tags, so unfortunately we have to break it up like this rather than just use a single
      // encoder for the overall GoodsItemIdentity type, to put those fields in the correct pos
      val commCode: NodeSeq = item.itemIdentity match {
        case id: GoodsItemIdentity.ByCommodityCode => id.toXml
        case _ => Nil
      }
      val desc: NodeSeq = item.itemIdentity match {
        case id: GoodsItemIdentity.WithDescription => id.toXml
        case _ => Nil
      }

      Seq(
        <IteNumGDS7>{item.itemNumber}</IteNumGDS7>,
        desc,
        grossMass,
        <MetOfPayGDI12>{item.paymentMethod.toXmlString}</MetOfPayGDI12>,
        dangerousGoodsCode,
        <PlaLoaGOOITE333>{item.placeOfLoading.toXmlString}</PlaLoaGOOITE333>,
        <PlaUnlGOOITE333>{item.placeOfUnloading.toXmlString}</PlaUnlGOOITE333>,
        documents,
        specialMentions,
        <TRACONCO2>{goodsConsignorFormat.encode(item.consignor)}</TRACONCO2>,
        commCode,
        consignee,
        containers,
        packages,
        notifiedParty
      ).flatten
    }

    override def decode(data: NodeSeq): GoodsItem = GoodsItem(
      itemNumber = (data \\ "IteNumGDS7").text.toInt,
      itemIdentity = data.parseXml[GoodsItemIdentity],
      grossMass = (data \\ "GroMasGDS46").headOption map { _.text.parseXmlString[BigDecimal] },
      paymentMethod = (data \\ "MetOfPayGDI12").text.parseXmlString[PaymentMethod],
      dangerousGoodsCode = (data \\ "UNDanGooCodGDI1").headOption map { _.text.parseXmlString[DangerousGoodsCode] },
      placeOfLoading = (data \\ "PlaLoaGOOITE333").text.parseXmlString[LoadingPlace],
      placeOfUnloading = (data \\ "PlaUnlGOOITE333").text.parseXmlString[LoadingPlace],
      documents = (data \\ "PRODOCDC2").map { _.parseXml[Document] }.toList,
      consignor = goodsConsignorFormat.decode(data \\ "TRACONCO2"),
      consignee = (data \\ "TRACONCE2").headOption.map(goodsConsigneeFormat.decode),
      notifiedParty = (data \\ "PRTNOT640").headOption.map(goodsNotifiedPartyFormat.decode),
      containers = (data \\ "CONNR2").map { _.parseXml[Container] }.toList,
      packages = (data \\ "PACGS2").map { _.parseXml[Package] }.toList,
      specialMentions = (data \\ "SPEMENMT2").map { _.text.parseXmlString[SpecialMention] }.toList
    )
  }

}
