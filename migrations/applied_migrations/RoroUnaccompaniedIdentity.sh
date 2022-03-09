#!/bin/bash

echo ""
echo "Applying migration RoroUnaccompaniedIdentity"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/roroUnaccompaniedIdentity                        controllers.transport.RoroUnaccompaniedIdentityController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/roroUnaccompaniedIdentity                        controllers.transport.RoroUnaccompaniedIdentityController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeRoroUnaccompaniedIdentity                  controllers.transport.RoroUnaccompaniedIdentityController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeRoroUnaccompaniedIdentity                  controllers.transport.RoroUnaccompaniedIdentityController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "roroUnaccompaniedIdentity.title = roroUnaccompaniedIdentity" >> ../conf/messages.en
echo "roroUnaccompaniedIdentity.heading = roroUnaccompaniedIdentity" >> ../conf/messages.en
echo "roroUnaccompaniedIdentity.field1 = field1" >> ../conf/messages.en
echo "roroUnaccompaniedIdentity.field2 = field2" >> ../conf/messages.en
echo "roroUnaccompaniedIdentity.checkYourAnswersLabel = RoroUnaccompaniedIdentity" >> ../conf/messages.en
echo "roroUnaccompaniedIdentity.error.field1.required = Enter field1" >> ../conf/messages.en
echo "roroUnaccompaniedIdentity.error.field2.required = Enter field2" >> ../conf/messages.en
echo "roroUnaccompaniedIdentity.error.field1.length = field1 must be 100 characters or less" >> ../conf/messages.en
echo "roroUnaccompaniedIdentity.error.field2.length = field2 must be 100 characters or less" >> ../conf/messages.en
echo "roroUnaccompaniedIdentity.field1.change.hidden = field1" >> ../conf/messages.en
echo "roroUnaccompaniedIdentity.field2.change.hidden = field2" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRoroUnaccompaniedIdentityUserAnswersEntry: Arbitrary[(RoroUnaccompaniedIdentityPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RoroUnaccompaniedIdentityPage.type]";\
    print "        value <- arbitrary[RoroUnaccompaniedIdentity].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRoroUnaccompaniedIdentityPage: Arbitrary[RoroUnaccompaniedIdentityPage.type] =";\
    print "    Arbitrary(RoroUnaccompaniedIdentityPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRoroUnaccompaniedIdentity: Arbitrary[RoroUnaccompaniedIdentity] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        field1 <- arbitrary[String]";\
    print "        field2 <- arbitrary[String]";\
    print "      } yield RoroUnaccompaniedIdentity(field1, field2)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RoroUnaccompaniedIdentityPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration RoroUnaccompaniedIdentity completed"
