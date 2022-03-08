#!/bin/bash

echo ""
echo "Applying migration RoadIdentity"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/roadIdentity                        controllers.transport.RoadIdentityController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/roadIdentity                        controllers.transport.RoadIdentityController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeRoadIdentity                  controllers.transport.RoadIdentityController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeRoadIdentity                  controllers.transport.RoadIdentityController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "roadIdentity.title = roadIdentity" >> ../conf/messages.en
echo "roadIdentity.heading = roadIdentity" >> ../conf/messages.en
echo "roadIdentity.field1 = field1" >> ../conf/messages.en
echo "roadIdentity.field2 = field2" >> ../conf/messages.en
echo "roadIdentity.checkYourAnswersLabel = RoadIdentity" >> ../conf/messages.en
echo "roadIdentity.error.field1.required = Enter field1" >> ../conf/messages.en
echo "roadIdentity.error.field2.required = Enter field2" >> ../conf/messages.en
echo "roadIdentity.error.field1.length = field1 must be 100 characters or less" >> ../conf/messages.en
echo "roadIdentity.error.field2.length = field2 must be 100 characters or less" >> ../conf/messages.en
echo "roadIdentity.field1.change.hidden = field1" >> ../conf/messages.en
echo "roadIdentity.field2.change.hidden = field2" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRoadIdentityUserAnswersEntry: Arbitrary[(RoadIdentityPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RoadIdentityPage.type]";\
    print "        value <- arbitrary[RoadIdentity].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRoadIdentityPage: Arbitrary[RoadIdentityPage.type] =";\
    print "    Arbitrary(RoadIdentityPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRoadIdentity: Arbitrary[RoadIdentity] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        field1 <- arbitrary[String]";\
    print "        field2 <- arbitrary[String]";\
    print "      } yield RoadIdentity(field1, field2)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RoadIdentityPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration RoadIdentity completed"
