#!/bin/bash

echo ""
echo "Applying migration CustomsOfficeOfFirstEntry"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/customsOfficeOfFirstEntry                        controllers.CustomsOfficeOfFirstEntryController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/customsOfficeOfFirstEntry                        controllers.CustomsOfficeOfFirstEntryController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeCustomsOfficeOfFirstEntry                  controllers.CustomsOfficeOfFirstEntryController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeCustomsOfficeOfFirstEntry                  controllers.CustomsOfficeOfFirstEntryController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "customsOfficeOfFirstEntry.title = customsOfficeOfFirstEntry" >> ../conf/messages.en
echo "customsOfficeOfFirstEntry.heading = customsOfficeOfFirstEntry" >> ../conf/messages.en
echo "customsOfficeOfFirstEntry.checkYourAnswersLabel = customsOfficeOfFirstEntry" >> ../conf/messages.en
echo "customsOfficeOfFirstEntry.error.required = Enter customsOfficeOfFirstEntry" >> ../conf/messages.en
echo "customsOfficeOfFirstEntry.error.length = CustomsOfficeOfFirstEntry must be 2 characters or less" >> ../conf/messages.en
echo "customsOfficeOfFirstEntry.change.hidden = CustomsOfficeOfFirstEntry" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCustomsOfficeOfFirstEntryUserAnswersEntry: Arbitrary[(CustomsOfficeOfFirstEntryPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[CustomsOfficeOfFirstEntryPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCustomsOfficeOfFirstEntryPage: Arbitrary[CustomsOfficeOfFirstEntryPage.type] =";\
    print "    Arbitrary(CustomsOfficeOfFirstEntryPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(CustomsOfficeOfFirstEntryPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration CustomsOfficeOfFirstEntry completed"
