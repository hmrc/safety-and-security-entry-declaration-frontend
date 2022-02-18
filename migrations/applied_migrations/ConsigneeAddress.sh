#!/bin/bash

echo ""
echo "Applying migration ConsigneeAddress"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/consigneeAddress                        controllers.ConsigneeAddressController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/consigneeAddress                        controllers.ConsigneeAddressController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeConsigneeAddress                  controllers.ConsigneeAddressController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeConsigneeAddress                  controllers.ConsigneeAddressController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "consigneeAddress.title = consigneeAddress" >> ../conf/messages.en
echo "consigneeAddress.heading = consigneeAddress" >> ../conf/messages.en
echo "consigneeAddress.checkYourAnswersLabel = consigneeAddress" >> ../conf/messages.en
echo "consigneeAddress.error.required = Enter consigneeAddress" >> ../conf/messages.en
echo "consigneeAddress.error.length = ConsigneeAddress must be 35 characters or less" >> ../conf/messages.en
echo "consigneeAddress.change.hidden = ConsigneeAddress" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsigneeAddressUserAnswersEntry: Arbitrary[(ConsigneeAddressPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ConsigneeAddressPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsigneeAddressPage: Arbitrary[ConsigneeAddressPage.type] =";\
    print "    Arbitrary(ConsigneeAddressPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ConsigneeAddressPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration ConsigneeAddress completed"
