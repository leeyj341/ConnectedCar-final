
package connected.car.period;
import java.util.List;

import connected.car.period.MyexpendVO;
import connected.car.period.TermVO;

public interface PeriodDAO {

	public List<MyexpendVO> getPeriodlist(String car_id);
	
	
}