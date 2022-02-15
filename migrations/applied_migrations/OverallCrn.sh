#!/bin/bash

echo ""
echo "Applying migration OverallCrn"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/overallCrn                        controllers.OverallCrnController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/overallCrn                        controllers.OverallCrnController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeOverallCrn                  controllers.OverallCrnController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeOverallCrn                  controllers.OverallCrnController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "overallCrn.title = overallCrn" >> ../conf/messages.en
echo "overallCrn.heading = overallCrn" >> ../conf/messages.en
echo "overallCrn.checkYourAnswersLabel = overallCrn" >> ../conf/messages.en
echo "overallCrn.error.required = Enter overallCrn" >> ../conf/messages.en
echo "overallCrn.error.length = OverallCrn must be 70 characters or less" >> ../conf/messages.en
echo "overallCrn.change.hidden = OverallCrn" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryOverallCrnUserAnswersEntry: Arbitrary[(OverallCrnPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[OverallCrnPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryOverallCrnPage: Arbitrary[OverallCrnPage.type] =";\
    print "    Arbitrary(OverallCrnPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(OverallCrnPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration OverallCrn completed"
