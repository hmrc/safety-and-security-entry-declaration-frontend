#!/bin/bash

echo ""
echo "Applying migration MaritimeIdentity"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/maritimeIdentity                        controllers.transport.MaritimeIdentityController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/maritimeIdentity                        controllers.transport.MaritimeIdentityController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeMaritimeIdentity                  controllers.transport.MaritimeIdentityController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeMaritimeIdentity                  controllers.transport.MaritimeIdentityController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "maritimeIdentity.title = maritimeIdentity" >> ../conf/messages.en
echo "maritimeIdentity.heading = maritimeIdentity" >> ../conf/messages.en
echo "maritimeIdentity.field1 = field1" >> ../conf/messages.en
echo "maritimeIdentity.field2 = field2" >> ../conf/messages.en
echo "maritimeIdentity.checkYourAnswersLabel = MaritimeIdentity" >> ../conf/messages.en
echo "maritimeIdentity.error.field1.required = Enter field1" >> ../conf/messages.en
echo "maritimeIdentity.error.field2.required = Enter field2" >> ../conf/messages.en
echo "maritimeIdentity.error.field1.length = field1 must be 100 characters or less" >> ../conf/messages.en
echo "maritimeIdentity.error.field2.length = field2 must be 100 characters or less" >> ../conf/messages.en
echo "maritimeIdentity.field1.change.hidden = field1" >> ../conf/messages.en
echo "maritimeIdentity.field2.change.hidden = field2" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMaritimeIdentityUserAnswersEntry: Arbitrary[(MaritimeIdentityPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[MaritimeIdentityPage.type]";\
    print "        value <- arbitrary[MaritimeIdentity].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMaritimeIdentityPage: Arbitrary[MaritimeIdentityPage.type] =";\
    print "    Arbitrary(MaritimeIdentityPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMaritimeIdentity: Arbitrary[MaritimeIdentity] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        field1 <- arbitrary[String]";\
    print "        field2 <- arbitrary[String]";\
    print "      } yield MaritimeIdentity(field1, field2)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(MaritimeIdentityPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration MaritimeIdentity completed"
