#!/bin/bash

echo ""
echo "Applying migration ArrivalDateAndTime"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/arrivalDateAndTime                        controllers.ArrivalDateAndTimeController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/arrivalDateAndTime                        controllers.ArrivalDateAndTimeController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeArrivalDateAndTime                  controllers.ArrivalDateAndTimeController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeArrivalDateAndTime                  controllers.ArrivalDateAndTimeController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "arrivalDateAndTime.title = arrivalDateAndTime" >> ../conf/messages.en
echo "arrivalDateAndTime.heading = arrivalDateAndTime" >> ../conf/messages.en
echo "arrivalDateAndTime.date = date" >> ../conf/messages.en
echo "arrivalDateAndTime.time = time" >> ../conf/messages.en
echo "arrivalDateAndTime.checkYourAnswersLabel = ArrivalDateAndTime" >> ../conf/messages.en
echo "arrivalDateAndTime.error.date.required = Enter date" >> ../conf/messages.en
echo "arrivalDateAndTime.error.time.required = Enter time" >> ../conf/messages.en
echo "arrivalDateAndTime.error.date.length = date must be 100 characters or less" >> ../conf/messages.en
echo "arrivalDateAndTime.error.time.length = time must be 100 characters or less" >> ../conf/messages.en
echo "arrivalDateAndTime.date.change.hidden = date" >> ../conf/messages.en
echo "arrivalDateAndTime.time.change.hidden = time" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryArrivalDateAndTimeUserAnswersEntry: Arbitrary[(ArrivalDateAndTimePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ArrivalDateAndTimePage.type]";\
    print "        value <- arbitrary[ArrivalDateAndTime].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryArrivalDateAndTimePage: Arbitrary[ArrivalDateAndTimePage.type] =";\
    print "    Arbitrary(ArrivalDateAndTimePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryArrivalDateAndTime: Arbitrary[ArrivalDateAndTime] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        date <- arbitrary[String]";\
    print "        time <- arbitrary[String]";\
    print "      } yield ArrivalDateAndTime(date, time)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ArrivalDateAndTimePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration ArrivalDateAndTime completed"
