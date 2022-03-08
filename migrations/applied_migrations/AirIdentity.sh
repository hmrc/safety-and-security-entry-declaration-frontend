#!/bin/bash

echo ""
echo "Applying migration AirIdentity"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/airIdentity                        controllers.transport.AirIdentityController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/airIdentity                        controllers.transport.AirIdentityController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeAirIdentity                  controllers.transport.AirIdentityController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeAirIdentity                  controllers.transport.AirIdentityController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "airIdentity.title = airIdentity" >> ../conf/messages.en
echo "airIdentity.heading = airIdentity" >> ../conf/messages.en
echo "airIdentity.field1 = field1" >> ../conf/messages.en
echo "airIdentity.field2 = field2" >> ../conf/messages.en
echo "airIdentity.checkYourAnswersLabel = AirIdentity" >> ../conf/messages.en
echo "airIdentity.error.field1.required = Enter field1" >> ../conf/messages.en
echo "airIdentity.error.field2.required = Enter field2" >> ../conf/messages.en
echo "airIdentity.error.field1.length = field1 must be 100 characters or less" >> ../conf/messages.en
echo "airIdentity.error.field2.length = field2 must be 100 characters or less" >> ../conf/messages.en
echo "airIdentity.field1.change.hidden = field1" >> ../conf/messages.en
echo "airIdentity.field2.change.hidden = field2" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAirIdentityUserAnswersEntry: Arbitrary[(AirIdentityPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AirIdentityPage.type]";\
    print "        value <- arbitrary[AirIdentity].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAirIdentityPage: Arbitrary[AirIdentityPage.type] =";\
    print "    Arbitrary(AirIdentityPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAirIdentity: Arbitrary[AirIdentity] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        field1 <- arbitrary[String]";\
    print "        field2 <- arbitrary[String]";\
    print "      } yield AirIdentity(field1, field2)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AirIdentityPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AirIdentity completed"
