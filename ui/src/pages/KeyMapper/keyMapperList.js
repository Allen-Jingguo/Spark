import { PageContainer } from '@ant-design/pro-components';
import {
  Space,
  Table,
  Tag,
  Button,
  Col,
  Form,
  Input,
  Row,
  Select,
  UpOutlined,
  DownOutlined,
  message,
  Popconfirm,
} from 'antd';
import {
  ProFormDatePicker,
  ProFormDateRangePicker,
  ProFormSelect,
  ProFormText,
  QueryFilter,
} from '@ant-design/pro-components';
import { Card } from 'antd';
import React, { useState, useEffect } from 'react';
import keyMapperApi from '@/services/spark/keyMapperApi';
import KeyMapperCreateModal from '@/pages/KeyMapper/keyMapperCreateModal';

const view = (record) => {
  keyMapperApi.view({ ...record }).then((result) => {
    console.log(result);
    if (result) {
      message.success('start success!');
    }
  });
};

const disable = (record) => {
  console.log(record);
  keyMapperApi.disable({ ...record }).then((result) => {
    console.log(result);
    if (result) {
      message.success('operate successfully!');
    }
  });
};

const cancel = (e) => {
  console.log(e);
  message.success('Canceled');
};

const columns = [
  // {
  //   title: 'Id',
  //   dataIndex: 'id',
  //   disable: true,
  // },
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
    render: (text) => <a>{text}</a>,
  },

  {
    title: 'Type',
    dataIndex: 'type',
  },

  {
    title: 'Created Time',
    dataIndex: 'createdTime',
  },

  {
    title: 'Action',
    key: 'action',
    render: (_, record) => (
      <Space size="middle">
        <Button type="primary" onClick={()=>{
          message.success('Wait for the implementation !');
        }}>Detail</Button>

        <Popconfirm
          title="Are you sure to start this flow?"
          onConfirm={() => disable(record)}
          onCancel={cancel}
          okText="Yes"
          cancelText="No"
        >
          {/* <a href="#" style={{ fontSize: 15 }}>
            disable
          </a> */}

          <Button type="primary">disable</Button>
        </Popconfirm>
      </Space>
    ),
  },
];

const FormatterList = () => {
  const [form] = Form.useForm();
  const [tableList, setTableList] = useState([]);
  const [allType, setAllType] = useState([]);
  const [showCreateModal, setShowCreateModal] = useState(false);

  useEffect(() => {
    keyMapperApi.getAllMapperType().then((resp) => {
      setAllType(resp.data);
    });
     keyMapperApi.getList({}).then((res) => {
       if (res.success) {
         setTableList(res.data);
       }
     });
  }, []);

  const doSearch = (values) => {
    keyMapperApi.getList(values).then((res) => {
      if (res.success) {
        setTableList(res.data);
      }
    });
  };

  const closeCreateModal = (success) => {
    if (success) {
      keyMapperApi.getList({}).then((res) => {
        if (res.success) {
          setTableList(res.data);
        }
      });
    }
    setShowCreateModal(false);
  };

  return (
    <PageContainer>
      <Card
        style={{
          borderRadius: 8,
        }}
        bodyStyle={{
          backgroundImage:
            'radial-gradient(circle at 97% 10%, #EBF2FF 0%, #F5F8FF 28%, #EBF1FF 124%)',
        }}
      >
        <div>
          <Form
            form={form}
            name="advanced_search"
            className="ant-advanced-search-form"
            onFinish={doSearch}
          >
            <Row gutter={24}>
              <Col>
                <Form.Item
                  name="name"
                  label="Namey"
                  // rules={[{ required: true, message: 'Missing name' }]}
                >
                  <Input />
                </Form.Item>
              </Col>
              <Col>
                <Form.Item name="type" label="Type">
                  <Select options={allType} style={{ width: 150 }} />
                </Form.Item>
              </Col>

              <Col
                //span={24}
                style={{
                  textAlign: 'left',
                }}
              >
                <Button type="primary" htmlType="submit">
                  Search
                </Button>
                <Button
                  style={{ marginLeft: 10 }}
                  type="primary"
                  onClick={() => {
                    form.resetFields();
                  }}
                >
                  Clear
                </Button>
                <Button
                  style={{ marginLeft: 10 }}
                  type="primary"
                  onClick={() => {
                    setShowCreateModal(true);
                  }}
                >
                  Create New
                </Button>
              </Col>
            </Row>
          </Form>
        </div>
        <div style={{ marginTop: 20 }}>
          <Table columns={columns} dataSource={tableList} pagination={true} />
        </div>
      </Card>
      {showCreateModal && <KeyMapperCreateModal closeCreateModal={closeCreateModal} />}
    </PageContainer>
  );
};
export default FormatterList;
