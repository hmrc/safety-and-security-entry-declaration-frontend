#!/bin/bash

echo ""
echo "Applying migration ItemContainerNumber"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/itemContainerNumber                        controllers.goods.ItemContainerNumberController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/itemContainerNumber                        controllers.goods.ItemContainerNumberController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeItemContainerNumber                  controllers.goods.ItemContainerNumberController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeItemContainerNumber                  controllers.goods.ItemContainerNumberController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "itemContainerNumber.title = itemContainerNumber" >> ../conf/messages.en
echo "itemContainerNumber.heading = itemContainerNumber" >> ../conf/messages.en
echo "itemContainerNumber.checkYourAnswersLabel = itemContainerNumber" >> ../conf/messages.en
echo "itemContainerNumber.error.required = Enter itemContainerNumber" >> ../conf/messages.en
echo "itemContainerNumber.error.length = ItemContainerNumber must be 20 characters or less" >> ../conf/messages.en
echo "itemContainerNumber.change.hidden = ItemContainerNumber" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryItemContainerNumberUserAnswersEntry: Arbitrary[(ItemContainerNumberPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ItemContainerNumberPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryItemContainerNumberPage: Arbitrary[ItemContainerNumberPage.type] =";\
    print "    Arbitrary(ItemContainerNumberPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ItemContainerNumberPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration ItemContainerNumber completed"
