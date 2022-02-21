#!/bin/bash

echo ""
echo "Applying migration AddPaymentMethod"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/addPaymentMethod                        controllers.AddPaymentMethodController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/addPaymentMethod                        controllers.AddPaymentMethodController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeAddPaymentMethod                  controllers.AddPaymentMethodController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeAddPaymentMethod                  controllers.AddPaymentMethodController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "addPaymentMethod.title = addPaymentMethod" >> ../conf/messages.en
echo "addPaymentMethod.heading = addPaymentMethod" >> ../conf/messages.en
echo "addPaymentMethod.checkYourAnswersLabel = addPaymentMethod" >> ../conf/messages.en
echo "addPaymentMethod.error.required = Select yes if addPaymentMethod" >> ../conf/messages.en
echo "addPaymentMethod.change.hidden = AddPaymentMethod" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddPaymentMethodUserAnswersEntry: Arbitrary[(AddPaymentMethodPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AddPaymentMethodPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddPaymentMethodPage: Arbitrary[AddPaymentMethodPage.type] =";\
    print "    Arbitrary(AddPaymentMethodPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AddPaymentMethodPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AddPaymentMethod completed"
