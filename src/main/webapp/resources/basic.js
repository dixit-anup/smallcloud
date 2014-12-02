/* getDateFormat: Date 객체를 "YYYY/MM/DD" 형태의 문자열로 변환 */
function getDateFormat(date) {
	var year = date.getFullYear(); // 연도를 Full Year(4자리) 형태로 가져온다 
	var month = date.getMonth() + 1;
	var dayOfMonth = date.getDate();
	
	// 포맷 조정
	if(month < 10) month = "0" + month; // 월이 10보다 작으면 앞에 0을 붙여준다
	if(dayOfMonth < 10) dayOfMonth = "0" + dayOfMonth; // 날짜가 10보다 작으면 앞에 0을 붙여준다
	
	return year + "/" + month + "/" + dayOfMonth;
}

/* getDatetimeFormat: Date 객체를 "YYYY/MM/DD AM|PM HH:MM" 형태의 문자열로 변환 */
function getDatetimeFormat(date) {
	var hour = date.getHours();
	var minutes = date.getMinutes();
	var ampm = "오전 ";
	
	// 시간 조정 및 오전 오후 설정
	if(hour == 0) {
		// 0이면 오전 12시(자정)
		hour = 12;
		ampm = "오전 ";
	} else if(hour == 12) {
		// 정오: ampm만 오후로 설정(오후 12시)
		ampm = "오후 ";
	} else if(hour > 12) {
		// 오후 시간대: 12진법 시간으로 바꿔준다
		hour %= 12;
		ampm = "오후 ";
	}
	
	// 포맷 조정
	if(hour < 10) hour = " " + hour; // 시간이 10보다 작으면 앞에 빈 문자를 붙여서 폭을 맞춘다
	if(minutes < 10) minutes = "0" + minutes; // 분이 10보다 작으면 앞에 0을 붙여준다
	
	return getDateFormat(date) + " " + ampm + hour + ":" + minutes;
}