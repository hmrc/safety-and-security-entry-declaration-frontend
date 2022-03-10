#!/bin/bash

echo ""
echo "Applying migration TransportCountryOrigin"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/transportCountryOrigin                        controllers.transport.TransportCountryOriginController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/transportCountryOrigin                        controllers.transport.TransportCountryOriginController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeTransportCountryOrigin                  controllers.transport.TransportCountryOriginController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeTransportCountryOrigin                  controllers.transport.TransportCountryOriginController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "transportCountryOrigin.title = transportCountryOrigin" >> ../conf/messages.en
echo "transportCountryOrigin.heading = transportCountryOrigin" >> ../conf/messages.en
echo "transportCountryOrigin.checkYourAnswersLabel = transportCountryOrigin" >> ../conf/messages.en
echo "transportCountryOrigin.error.required = Enter transportCountryOrigin" >> ../conf/messages.en
echo "transportCountryOrigin.error.length = TransportCountryOrigin must be 100 characters or less" >> ../conf/messages.en
echo "transportCountryOrigin.change.hidden = TransportCountryOrigin" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTransportCountryOriginUserAnswersEntry: Arbitrary[(TransportCountryOriginPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[TransportCountryOriginPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTransportCountryOriginPage: Arbitrary[TransportCountryOriginPage.type] =";\
    print "    Arbitrary(TransportCountryOriginPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(TransportCountryOriginPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration TransportCountryOrigin completed"
