#!/bin/bash

echo ""
echo "Applying migration MarkOrNumber"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/markOrNumber                        controllers.MarkOrNumberController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/markOrNumber                        controllers.MarkOrNumberController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeMarkOrNumber                  controllers.MarkOrNumberController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeMarkOrNumber                  controllers.MarkOrNumberController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "markOrNumber.title = markOrNumber" >> ../conf/messages.en
echo "markOrNumber.heading = markOrNumber" >> ../conf/messages.en
echo "markOrNumber.checkYourAnswersLabel = markOrNumber" >> ../conf/messages.en
echo "markOrNumber.error.required = Enter markOrNumber" >> ../conf/messages.en
echo "markOrNumber.error.length = MarkOrNumber must be 140 characters or less" >> ../conf/messages.en
echo "markOrNumber.change.hidden = MarkOrNumber" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMarkOrNumberUserAnswersEntry: Arbitrary[(MarkOrNumberPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[MarkOrNumberPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMarkOrNumberPage: Arbitrary[MarkOrNumberPage.type] =";\
    print "    Arbitrary(MarkOrNumberPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(MarkOrNumberPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration MarkOrNumber completed"
