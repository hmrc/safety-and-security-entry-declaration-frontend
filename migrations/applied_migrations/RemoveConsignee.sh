#!/bin/bash

echo ""
echo "Applying migration RemoveConsignee"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/removeConsignee                        controllers.consignees.RemoveConsigneeController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/removeConsignee                        controllers.consignees.RemoveConsigneeController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeRemoveConsignee                  controllers.consignees.RemoveConsigneeController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeRemoveConsignee                  controllers.consignees.RemoveConsigneeController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "removeConsignee.title = removeConsignee" >> ../conf/messages.en
echo "removeConsignee.heading = removeConsignee" >> ../conf/messages.en
echo "removeConsignee.checkYourAnswersLabel = removeConsignee" >> ../conf/messages.en
echo "removeConsignee.error.required = Select yes if removeConsignee" >> ../conf/messages.en
echo "removeConsignee.change.hidden = RemoveConsignee" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRemoveConsigneeUserAnswersEntry: Arbitrary[(RemoveConsigneePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RemoveConsigneePage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRemoveConsigneePage: Arbitrary[RemoveConsigneePage.type] =";\
    print "    Arbitrary(RemoveConsigneePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RemoveConsigneePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration RemoveConsignee completed"
