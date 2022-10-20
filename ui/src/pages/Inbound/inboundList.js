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
import inboundApi from '@/services/spark/inboundApi';
import InboundCreateModal from '@/pages/Inbound/inboundCreateModal';

const view = (record) => {
  inboundApi.view({ ...record }).then((result) => {
    console.log(result);
    if (result) {
      message.success('start success!');
    }
  });
  message.success('operate successfully');
};

const disable = (record) => {
  console.log(record);
  inboundApi.disable({ ...record }).then((result) => {
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
    dataIndex: 'inboundType',
  },

  {
    title: 'HostName',
    dataIndex: 'hostName',
  },

  {
    title: 'User Name',
    dataIndex: 'userName',
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
        <Button
          type="primary"
          onClick={() => {
            message.success('Wait for the implementation !');
          }}
        >
          Detail
        </Button>

        <Popconfirm
          title="Are you sure to start this flow?"
          onConfirm={() => disable(record)}
          onCancel={cancel}
          okText="Yes"
          cancelText="No"
        >
          <Button type="primary">disable</Button>
        </Popconfirm>
      </Space>
    ),
  },
];

const InboundList = () => {
  const [form] = Form.useForm();
  const [tableList, setTableList] = useState([]);
  const [showCreateModal, setShowCreateModal] = useState(false);
   const [allInboundType, setAllInboundType] = useState([]);

  useEffect(() => {
     inboundApi.getAllInboundType().then((resp) => {
       if (resp.success) {
         setAllInboundType(resp.data);
       }
     });
    inboundApi.getList({}).then((res) => {
      if (res) {
        setTableList(res.data);
      }
    });
  }, []);

  const doSearch = (values) => {
    inboundApi.getList(values).then((res) => {
      if (res) {
        setTableList(res.data);
      }
    });
  };

  const closeCreateModal = (success) => {
    if (success) {
      inboundApi.getList({}).then((res) => {
        if (res) {
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
                  label="Name"
                  // rules={[{ required: true, message: 'Missing name' }]}
                >
                  <Input />
                </Form.Item>
              </Col>
              <Col>
                <Form.Item name="type" label="Type">
                  <Select options={allInboundType} style={{ width: 150 }} />
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
      {showCreateModal && <InboundCreateModal closeCreateModal={closeCreateModal} />}
    </PageContainer>
  );
};
export default InboundList;
