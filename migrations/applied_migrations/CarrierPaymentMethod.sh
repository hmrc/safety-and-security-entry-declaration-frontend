#!/bin/bash

echo ""
echo "Applying migration PaymentMethod"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/paymentMethod                        controllers.PaymentMethodController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/paymentMethod                        controllers.PaymentMethodController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changePaymentMethod                  controllers.PaymentMethodController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changePaymentMethod                  controllers.PaymentMethodController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "paymentMethod.title = How will the carrier be paid?" >> ../conf/messages.en
echo "paymentMethod.heading = How will the carrier be paid?" >> ../conf/messages.en
echo "paymentMethod.cash = Payment in cash" >> ../conf/messages.en
echo "paymentMethod.credit_card = Payment by credit card" >> ../conf/messages.en
echo "paymentMethod.checkYourAnswersLabel = How will the carrier be paid?" >> ../conf/messages.en
echo "paymentMethod.error.required = Select paymentMethod" >> ../conf/messages.en
echo "paymentMethod.change.hidden = PaymentMethod" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPaymentMethodUserAnswersEntry: Arbitrary[(PaymentMethodPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[PaymentMethodPage.type]";\
    print "        value <- arbitrary[PaymentMethod].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPaymentMethodPage: Arbitrary[PaymentMethodPage.type] =";\
    print "    Arbitrary(PaymentMethodPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPaymentMethod: Arbitrary[PaymentMethod] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(PaymentMethod.values.toSeq)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(PaymentMethodPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration PaymentMethod completed"
