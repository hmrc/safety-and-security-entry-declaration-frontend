#!/bin/bash

echo ""
echo "Applying migration OverallCrnKnown"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/overallCrnKnown                        controllers.OverallCrnKnownController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/overallCrnKnown                        controllers.OverallCrnKnownController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeOverallCrnKnown                  controllers.OverallCrnKnownController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeOverallCrnKnown                  controllers.OverallCrnKnownController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "overallCrnKnown.title = overallCrnKnown" >> ../conf/messages.en
echo "overallCrnKnown.heading = overallCrnKnown" >> ../conf/messages.en
echo "overallCrnKnown.checkYourAnswersLabel = overallCrnKnown" >> ../conf/messages.en
echo "overallCrnKnown.error.required = Select yes if overallCrnKnown" >> ../conf/messages.en
echo "overallCrnKnown.change.hidden = OverallCrnKnown" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryOverallCrnKnownUserAnswersEntry: Arbitrary[(OverallCrnKnownPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[OverallCrnKnownPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryOverallCrnKnownPage: Arbitrary[OverallCrnKnownPage.type] =";\
    print "    Arbitrary(OverallCrnKnownPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(OverallCrnKnownPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration OverallCrnKnown completed"
