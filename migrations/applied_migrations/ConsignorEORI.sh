#!/bin/bash

echo ""
echo "Applying migration ConsignorEORI"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/consignorEORI                        controllers.ConsignorEORIController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/consignorEORI                        controllers.ConsignorEORIController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeConsignorEORI                  controllers.ConsignorEORIController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeConsignorEORI                  controllers.ConsignorEORIController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "consignorEORI.title = consignorEORI" >> ../conf/messages.en
echo "consignorEORI.heading = consignorEORI" >> ../conf/messages.en
echo "consignorEORI.checkYourAnswersLabel = consignorEORI" >> ../conf/messages.en
echo "consignorEORI.error.required = Enter consignorEORI" >> ../conf/messages.en
echo "consignorEORI.error.length = ConsignorEORI must be 50 characters or less" >> ../conf/messages.en
echo "consignorEORI.change.hidden = ConsignorEORI" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsignorEORIUserAnswersEntry: Arbitrary[(ConsignorEORIPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ConsignorEORIPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsignorEORIPage: Arbitrary[ConsignorEORIPage.type] =";\
    print "    Arbitrary(ConsignorEORIPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ConsignorEORIPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration ConsignorEORI completed"
