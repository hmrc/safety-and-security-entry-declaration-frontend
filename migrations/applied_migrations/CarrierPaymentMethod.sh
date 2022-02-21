#!/bin/bash

echo ""
echo "Applying migration CarrierPaymentMethod"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/carrierPaymentMethod                        controllers.CarrierPaymentMethodController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/carrierPaymentMethod                        controllers.CarrierPaymentMethodController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeCarrierPaymentMethod                  controllers.CarrierPaymentMethodController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeCarrierPaymentMethod                  controllers.CarrierPaymentMethodController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "carrierPaymentMethod.title = How will the carrier be paid?" >> ../conf/messages.en
echo "carrierPaymentMethod.heading = How will the carrier be paid?" >> ../conf/messages.en
echo "carrierPaymentMethod.cash = Payment in cash" >> ../conf/messages.en
echo "carrierPaymentMethod.credit_card = Payment by credit card" >> ../conf/messages.en
echo "carrierPaymentMethod.checkYourAnswersLabel = How will the carrier be paid?" >> ../conf/messages.en
echo "carrierPaymentMethod.error.required = Select carrierPaymentMethod" >> ../conf/messages.en
echo "carrierPaymentMethod.change.hidden = CarrierPaymentMethod" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCarrierPaymentMethodUserAnswersEntry: Arbitrary[(CarrierPaymentMethodPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[CarrierPaymentMethodPage.type]";\
    print "        value <- arbitrary[CarrierPaymentMethod].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCarrierPaymentMethodPage: Arbitrary[CarrierPaymentMethodPage.type] =";\
    print "    Arbitrary(CarrierPaymentMethodPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCarrierPaymentMethod: Arbitrary[CarrierPaymentMethod] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(CarrierPaymentMethod.values.toSeq)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(CarrierPaymentMethodPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration CarrierPaymentMethod completed"
