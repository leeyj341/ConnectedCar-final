package connected.car.admin;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("adminDAO")
public class AdminDAOImpl implements AdminDAO {
	@Autowired
	SqlSession sqlsession;
	
	@Override
	public int expendableAdd(ExpendableVO expendableVO) {
		return sqlsession.insert("connected.car.admin.insertExpendable", expendableVO);
	}

	@Override
	public List<ExpendableVO> listAll(Pagination pagination) {
		//System.out.println(pagination);
		List<ExpendableVO> list = sqlsession.selectList("connected.car.admin.listAll", pagination);
		//System.out.println(list);
		return list;
	}
	
	@Override
	public int listAllCnt() {
		return sqlsession.selectOne("connected.car.admin.listAllCnt");
	}
	
	@Override
	public int expendableDelete(String expend_id) {
		//System.out.println("DAO:"+expend_id);
		return sqlsession.delete("connected.car.admin.deleteExpendable", expend_id);
	}

}
