#!/bin/bash

echo ""
echo "Applying migration DangerousGoodCode"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/dangerousGoodCode                        controllers.DangerousGoodCodeController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/dangerousGoodCode                        controllers.DangerousGoodCodeController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeDangerousGoodCode                  controllers.DangerousGoodCodeController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeDangerousGoodCode                  controllers.DangerousGoodCodeController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "dangerousGoodCode.title = dangerousGoodCode" >> ../conf/messages.en
echo "dangerousGoodCode.heading = dangerousGoodCode" >> ../conf/messages.en
echo "dangerousGoodCode.checkYourAnswersLabel = dangerousGoodCode" >> ../conf/messages.en
echo "dangerousGoodCode.error.required = Enter dangerousGoodCode" >> ../conf/messages.en
echo "dangerousGoodCode.error.length = DangerousGoodCode must be 200 characters or less" >> ../conf/messages.en
echo "dangerousGoodCode.change.hidden = DangerousGoodCode" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDangerousGoodCodeUserAnswersEntry: Arbitrary[(DangerousGoodCodePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DangerousGoodCodePage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDangerousGoodCodePage: Arbitrary[DangerousGoodCodePage.type] =";\
    print "    Arbitrary(DangerousGoodCodePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DangerousGoodCodePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration DangerousGoodCode completed"
