#!/bin/bash

echo ""
echo "Applying migration RailIdentity"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/railIdentity                        controllers.transport.RailIdentityController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/railIdentity                        controllers.transport.RailIdentityController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeRailIdentity                  controllers.transport.RailIdentityController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeRailIdentity                  controllers.transport.RailIdentityController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "railIdentity.title = railIdentity" >> ../conf/messages.en
echo "railIdentity.heading = railIdentity" >> ../conf/messages.en
echo "railIdentity.field1 = field1" >> ../conf/messages.en
echo "railIdentity.field2 = field2" >> ../conf/messages.en
echo "railIdentity.checkYourAnswersLabel = RailIdentity" >> ../conf/messages.en
echo "railIdentity.error.field1.required = Enter field1" >> ../conf/messages.en
echo "railIdentity.error.field2.required = Enter field2" >> ../conf/messages.en
echo "railIdentity.error.field1.length = field1 must be 100 characters or less" >> ../conf/messages.en
echo "railIdentity.error.field2.length = field2 must be 100 characters or less" >> ../conf/messages.en
echo "railIdentity.field1.change.hidden = field1" >> ../conf/messages.en
echo "railIdentity.field2.change.hidden = field2" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRailIdentityUserAnswersEntry: Arbitrary[(RailIdentityPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RailIdentityPage.type]";\
    print "        value <- arbitrary[RailIdentity].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRailIdentityPage: Arbitrary[RailIdentityPage.type] =";\
    print "    Arbitrary(RailIdentityPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRailIdentity: Arbitrary[RailIdentity] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        field1 <- arbitrary[String]";\
    print "        field2 <- arbitrary[String]";\
    print "      } yield RailIdentity(field1, field2)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RailIdentityPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration RailIdentity completed"
