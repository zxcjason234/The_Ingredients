<?php
require "DataBaseConfig.php";

class DataBase
{
    public $connect;
    public $data;
    private $sql;
    protected $servername;
    protected $username;
    protected $password;
    protected $databasename;

    public function __construct()
    {
        $this->connect = null;
        $this->data = null;
        $this->sql = null;
        $dbc = new DataBaseConfig();
        $this->servername = $dbc->servername;
        $this->username = $dbc->username;
        $this->password = $dbc->password;
        $this->databasename = $dbc->databasename;
    }

    function dbConnect()
    {
        $this->connect = mysqli_connect($this->servername, $this->username, $this->password, $this->databasename);
        return $this->connect;
    }

    function prepareData($data)
    {
        return mysqli_real_escape_string($this->connect, stripslashes(htmlspecialchars($data)));
    }

    function logIn($table, $member_id, $password)
    {
        $member_id = $this->prepareData($member_id);
        $password = $this->prepareData($password);
        $this->sql = "select * from " . $table . " where member_id = '" . $member_id . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if  (mysqli_num_rows($result) != 0) {
            $dbusername = $row['member_id'];
            $dbpassword = $row['password'];
            if ($dbusername == $member_id && password_verify($password, $dbpassword)) {
                $login = true;
            } else $login = false;
        } else $login = false;

        return $login;
    }

    function signUp($table, $member_id, $member_name, $password)
    {
		$member_id = $this->prepareData($member_id);
        $member_name = $this->prepareData($member_name);
        $password = $this->prepareData($password);
        $password = password_hash($password, PASSWORD_DEFAULT);
        $this->sql =
            "INSERT INTO " . $table . " (member_id, member_name, password) VALUES ('" . $member_id . "','" . $member_name . "','" . $password . "')";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }
	
	function onlyfridge($table, $FridgeID, $FridgeName)
    {
        $FridgeID = $this->prepareData($FridgeID);
        $FridgeName = $this->prepareData($FridgeName);
        $this->sql =
            "INSERT INTO " . $table . " (FridgeID, FridgeName) VALUES ('" . $FridgeID . "','" . $FridgeName . "')";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }
	
	function fridge($table, $member_id, $FridgeID)
    {
		$member_id = $this->prepareData($member_id);
        $FridgeID = $this->prepareData($FridgeID);
		$today = date("Y-m-d");
        $this->sql =
            "INSERT INTO " . $table . " (member_id, FridgeID) VALUES ('" . $member_id . "','" . $FridgeID . "')";
        if (mysqli_query($this->connect, $this->sql)) {
			"INSERT INTO `fridge_note` (`note_id`, `Fridge_ID`, `member_id`, `note`, `upload_date`) VALUES (NULL, '$FridgeID', '$member_id', '開始使用備忘錄', '$today')"; 
            return true;
        } else return false;
    }
	
	
	function fridge_note($table, $member_id, $FridgeID)
    {
		$member_id = $this->prepareData($member_id);
        $FridgeID = $this->prepareData($FridgeID);
		$today = date("Y-m-d");
        $this->sql =
            "INSERT INTO `fridge_note` (`note_id`, `Fridge_ID`, `member_id`, `note`, `upload_date`) VALUES (NULL, '$FridgeID', '$member_id', '開始使用備忘錄', '$today')"; 			
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }
	


    function afterlogin_fridge($table, $member_id)
    {
        $member_id = $this->prepareData($member_id);
        $this->sql = "select * from " . $table . " where member_id = '" . $member_id . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if  (mysqli_num_rows($result) != 0) {
            $dbusername = $row['member_id'];
            if ($dbusername == $member_id ) {
                $login = true;
            } else $login = false;
        } else $login = false;

        return $login;
    }
	
	function delete_fridge($table, $member_id)
    {
		$member_id = $this->prepareData($member_id);
        $this->sql ="DELETE from " . $table . " where member_id = '" . $member_id . "'";

        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }
	
	function delete_note($table, $member_id)
    {
		$member_id = $this->prepareData($member_id);
        $this->sql ="DELETE from " . $table . " where member_id = '" . $member_id . "'";

        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }

}

?>
