#!/bin/bash

echo ""
echo "Applying migration LocalReferenceNumber"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /localReferenceNumber                        controllers.LocalReferenceNumberController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /localReferenceNumber                        controllers.LocalReferenceNumberController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeLocalReferenceNumber                  controllers.LocalReferenceNumberController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeLocalReferenceNumber                  controllers.LocalReferenceNumberController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "localReferenceNumber.title = localReferenceNumber" >> ../conf/messages.en
echo "localReferenceNumber.heading = localReferenceNumber" >> ../conf/messages.en
echo "localReferenceNumber.checkYourAnswersLabel = localReferenceNumber" >> ../conf/messages.en
echo "localReferenceNumber.error.required = Enter localReferenceNumber" >> ../conf/messages.en
echo "localReferenceNumber.error.length = LocalReferenceNumber must be 22 characters or less" >> ../conf/messages.en
echo "localReferenceNumber.change.hidden = LocalReferenceNumber" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryLocalReferenceNumberUserAnswersEntry: Arbitrary[(LocalReferenceNumberPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[LocalReferenceNumberPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryLocalReferenceNumberPage: Arbitrary[LocalReferenceNumberPage.type] =";\
    print "    Arbitrary(LocalReferenceNumberPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(LocalReferenceNumberPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Migration LocalReferenceNumber completed"
