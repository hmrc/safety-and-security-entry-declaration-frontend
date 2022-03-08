#!/bin/bash

echo ""
echo "Applying migration RoroAccompaniedIdentity"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/roroAccompaniedIdentity                        controllers.transport.RoroAccompaniedIdentityController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/roroAccompaniedIdentity                        controllers.transport.RoroAccompaniedIdentityController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeRoroAccompaniedIdentity                  controllers.transport.RoroAccompaniedIdentityController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeRoroAccompaniedIdentity                  controllers.transport.RoroAccompaniedIdentityController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "roroAccompaniedIdentity.title = roroAccompaniedIdentity" >> ../conf/messages.en
echo "roroAccompaniedIdentity.heading = roroAccompaniedIdentity" >> ../conf/messages.en
echo "roroAccompaniedIdentity.field1 = field1" >> ../conf/messages.en
echo "roroAccompaniedIdentity.field2 = field2" >> ../conf/messages.en
echo "roroAccompaniedIdentity.checkYourAnswersLabel = RoroAccompaniedIdentity" >> ../conf/messages.en
echo "roroAccompaniedIdentity.error.field1.required = Enter field1" >> ../conf/messages.en
echo "roroAccompaniedIdentity.error.field2.required = Enter field2" >> ../conf/messages.en
echo "roroAccompaniedIdentity.error.field1.length = field1 must be 100 characters or less" >> ../conf/messages.en
echo "roroAccompaniedIdentity.error.field2.length = field2 must be 100 characters or less" >> ../conf/messages.en
echo "roroAccompaniedIdentity.field1.change.hidden = field1" >> ../conf/messages.en
echo "roroAccompaniedIdentity.field2.change.hidden = field2" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRoroAccompaniedIdentityUserAnswersEntry: Arbitrary[(RoroAccompaniedIdentityPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RoroAccompaniedIdentityPage.type]";\
    print "        value <- arbitrary[RoroAccompaniedIdentity].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRoroAccompaniedIdentityPage: Arbitrary[RoroAccompaniedIdentityPage.type] =";\
    print "    Arbitrary(RoroAccompaniedIdentityPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRoroAccompaniedIdentity: Arbitrary[RoroAccompaniedIdentity] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        field1 <- arbitrary[String]";\
    print "        field2 <- arbitrary[String]";\
    print "      } yield RoroAccompaniedIdentity(field1, field2)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RoroAccompaniedIdentityPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration RoroAccompaniedIdentity completed"
