#!/bin/bash

echo ""
echo "Applying migration ShippingContainers"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/shippingContainers                        controllers.goods.ShippingContainersController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/shippingContainers                        controllers.goods.ShippingContainersController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeShippingContainers                  controllers.goods.ShippingContainersController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeShippingContainers                  controllers.goods.ShippingContainersController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "shippingContainers.title = shippingContainers" >> ../conf/messages.en
echo "shippingContainers.heading = shippingContainers" >> ../conf/messages.en
echo "shippingContainers.checkYourAnswersLabel = shippingContainers" >> ../conf/messages.en
echo "shippingContainers.error.required = Select yes if shippingContainers" >> ../conf/messages.en
echo "shippingContainers.change.hidden = ShippingContainers" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryShippingContainersUserAnswersEntry: Arbitrary[(ShippingContainersPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ShippingContainersPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryShippingContainersPage: Arbitrary[ShippingContainersPage.type] =";\
    print "    Arbitrary(ShippingContainersPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ShippingContainersPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration ShippingContainers completed"
