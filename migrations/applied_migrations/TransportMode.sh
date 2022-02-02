#!/bin/bash

echo ""
echo "Applying migration TransportMode"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /transportMode                        controllers.TransportModeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /transportMode                        controllers.TransportModeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeTransportMode                  controllers.TransportModeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeTransportMode                  controllers.TransportModeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "transportMode.title = What is the mode of transport?" >> ../conf/messages.en
echo "transportMode.heading = What is the mode of transport?" >> ../conf/messages.en
echo "transportMode.maritime = Maritime" >> ../conf/messages.en
echo "transportMode.other = other" >> ../conf/messages.en
echo "transportMode.checkYourAnswersLabel = What is the mode of transport?" >> ../conf/messages.en
echo "transportMode.error.required = Select transportMode" >> ../conf/messages.en
echo "transportMode.change.hidden = TransportMode" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTransportModeUserAnswersEntry: Arbitrary[(TransportModePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[TransportModePage.type]";\
    print "        value <- arbitrary[TransportMode].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTransportModePage: Arbitrary[TransportModePage.type] =";\
    print "    Arbitrary(TransportModePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTransportMode: Arbitrary[TransportMode] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(TransportMode.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(TransportModePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Migration TransportMode completed"
