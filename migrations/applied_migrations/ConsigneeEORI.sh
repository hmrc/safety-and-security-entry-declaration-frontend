#!/bin/bash

echo ""
echo "Applying migration ConsigneeEORI"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/consigneeEORI                        controllers.ConsigneeEORIController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/consigneeEORI                        controllers.ConsigneeEORIController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeConsigneeEORI                  controllers.ConsigneeEORIController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeConsigneeEORI                  controllers.ConsigneeEORIController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "consigneeEORI.title = consigneeEORI" >> ../conf/messages.en
echo "consigneeEORI.heading = consigneeEORI" >> ../conf/messages.en
echo "consigneeEORI.checkYourAnswersLabel = consigneeEORI" >> ../conf/messages.en
echo "consigneeEORI.error.required = Enter consigneeEORI" >> ../conf/messages.en
echo "consigneeEORI.error.length = ConsigneeEORI must be 35 characters or less" >> ../conf/messages.en
echo "consigneeEORI.change.hidden = ConsigneeEORI" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsigneeEORIUserAnswersEntry: Arbitrary[(ConsigneeEORIPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ConsigneeEORIPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsigneeEORIPage: Arbitrary[ConsigneeEORIPage.type] =";\
    print "    Arbitrary(ConsigneeEORIPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ConsigneeEORIPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration ConsigneeEORI completed"
