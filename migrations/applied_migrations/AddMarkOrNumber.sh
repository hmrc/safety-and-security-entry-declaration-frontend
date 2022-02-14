#!/bin/bash

echo ""
echo "Applying migration AddMarkOrNumber"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/addMarkOrNumber                        controllers.AddMarkOrNumberController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/addMarkOrNumber                        controllers.AddMarkOrNumberController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeAddMarkOrNumber                  controllers.AddMarkOrNumberController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeAddMarkOrNumber                  controllers.AddMarkOrNumberController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "addMarkOrNumber.title = addMarkOrNumber" >> ../conf/messages.en
echo "addMarkOrNumber.heading = addMarkOrNumber" >> ../conf/messages.en
echo "addMarkOrNumber.checkYourAnswersLabel = addMarkOrNumber" >> ../conf/messages.en
echo "addMarkOrNumber.error.required = Select yes if addMarkOrNumber" >> ../conf/messages.en
echo "addMarkOrNumber.change.hidden = AddMarkOrNumber" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddMarkOrNumberUserAnswersEntry: Arbitrary[(AddMarkOrNumberPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AddMarkOrNumberPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddMarkOrNumberPage: Arbitrary[AddMarkOrNumberPage.type] =";\
    print "    Arbitrary(AddMarkOrNumberPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AddMarkOrNumberPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AddMarkOrNumber completed"
