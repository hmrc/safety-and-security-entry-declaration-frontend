#!/bin/bash

echo ""
echo "Applying migration AddAnySeals"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/addAnySeals                        controllers.transport.AddAnySealsController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/addAnySeals                        controllers.transport.AddAnySealsController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeAddAnySeals                  controllers.transport.AddAnySealsController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeAddAnySeals                  controllers.transport.AddAnySealsController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "addAnySeals.title = addAnySeals" >> ../conf/messages.en
echo "addAnySeals.heading = addAnySeals" >> ../conf/messages.en
echo "addAnySeals.checkYourAnswersLabel = addAnySeals" >> ../conf/messages.en
echo "addAnySeals.error.required = Select yes if addAnySeals" >> ../conf/messages.en
echo "addAnySeals.change.hidden = AddAnySeals" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddAnySealsUserAnswersEntry: Arbitrary[(AddAnySealsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AddAnySealsPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddAnySealsPage: Arbitrary[AddAnySealsPage.type] =";\
    print "    Arbitrary(AddAnySealsPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AddAnySealsPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AddAnySeals completed"
