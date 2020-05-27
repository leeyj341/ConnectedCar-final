package connected.car.expendable;

import java.util.List;

import connected.car.admin.Pagination;
import connected.car.inventory.ExpendableVO;

public interface ExpendableDAO {
	public ExpendableVO findExpendable(String code, String brand);
	public List<ShopExpendableVO> findShopExpendableList(String shop_id, Pagination pagination);
	public int insertShopExpendable(String shop_id, ShopExpendableVO vo);
	public List<ExpendableLogVO> findExpendableLogList(String shop_id, String expend_id);
	public int insertExpendableLog(ExpendableLogVO log);
	public int getAllCnt(String shop_id);
}
