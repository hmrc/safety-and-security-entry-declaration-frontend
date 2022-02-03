#!/bin/bash

echo ""
echo "Applying migration CarriersEORI"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/carriersEORI                        controllers.CarriersEORIController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/carriersEORI                        controllers.CarriersEORIController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeCarriersEORI                  controllers.CarriersEORIController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeCarriersEORI                  controllers.CarriersEORIController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "carriersEORI.title = carriersEORI" >> ../conf/messages.en
echo "carriersEORI.heading = carriersEORI" >> ../conf/messages.en
echo "carriersEORI.checkYourAnswersLabel = carriersEORI" >> ../conf/messages.en
echo "carriersEORI.error.required = Enter carriersEORI" >> ../conf/messages.en
echo "carriersEORI.error.length = CarriersEORI must be 100 characters or less" >> ../conf/messages.en
echo "carriersEORI.change.hidden = CarriersEORI" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCarriersEORIUserAnswersEntry: Arbitrary[(CarriersEORIPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[CarriersEORIPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCarriersEORIPage: Arbitrary[CarriersEORIPage.type] =";\
    print "    Arbitrary(CarriersEORIPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(CarriersEORIPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Migration CarriersEORI completed"
