#!/bin/bash

echo ""
echo "Applying migration UnloadingCode"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/unloadingCode                        controllers.UnloadingCodeController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/unloadingCode                        controllers.UnloadingCodeController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeUnloadingCode                  controllers.UnloadingCodeController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeUnloadingCode                  controllers.UnloadingCodeController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "unloadingCode.title = unloadingCode" >> ../conf/messages.en
echo "unloadingCode.heading = unloadingCode" >> ../conf/messages.en
echo "unloadingCode.checkYourAnswersLabel = unloadingCode" >> ../conf/messages.en
echo "unloadingCode.error.required = Enter unloadingCode" >> ../conf/messages.en
echo "unloadingCode.error.length = UnloadingCode must be 10 characters or less" >> ../conf/messages.en
echo "unloadingCode.change.hidden = UnloadingCode" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryUnloadingCodeUserAnswersEntry: Arbitrary[(UnloadingCodePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[UnloadingCodePage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryUnloadingCodePage: Arbitrary[UnloadingCodePage.type] =";\
    print "    Arbitrary(UnloadingCodePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(UnloadingCodePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration UnloadingCode completed"
